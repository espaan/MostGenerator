package org.zikula.modulestudio.generator.cartridges.zclassic.controller.apis

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import org.eclipse.xtext.generator.IFileSystemAccess
import org.zikula.modulestudio.generator.cartridges.zclassic.smallstuff.FileHelper
import org.zikula.modulestudio.generator.cartridges.zclassic.view.additions.SearchView
import org.zikula.modulestudio.generator.extensions.ControllerExtensions
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.ModelExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.Utils

class Search {
    @Inject extension ControllerExtensions = new ControllerExtensions()
    @Inject extension FormattingExtensions = new FormattingExtensions()
    @Inject extension ModelExtensions = new ModelExtensions()
    @Inject extension NamingExtensions = new NamingExtensions()
    @Inject extension Utils = new Utils()

    FileHelper fh = new FileHelper()

    def generate(Application it, IFileSystemAccess fsa) {
        val apiPath = appName.getAppSourceLibPath + 'Api/'
        fsa.generateFile(apiPath + 'Base/Search.php', searchApiBaseFile)
        fsa.generateFile(apiPath + 'Search.php', searchApiFile)
        new SearchView().generate(it, fsa)
    }

    def private searchApiBaseFile(Application it) '''
    	«fh.phpFileHeader(it)»
    	«searchApiBaseClass»
    '''

    def private searchApiFile(Application it) '''
    	«fh.phpFileHeader(it)»
    	«searchApiImpl»
    '''

    def private searchApiBaseClass(Application it) '''
		/**
		 * Search api base class.
		 */
		class «appName»_Api_Base_Search extends Zikula_AbstractApi
		{
		    «searchApiBaseImpl»
		}
    '''

    def private searchApiBaseImpl(Application it) '''
        «info»
        
        «options»
        
        «search»
        
        «searchCheck»
    '''

    def private info(Application it) '''
        /**
         * Get search plugin information.
         *
         * @return array The search plugin information
         */
        public function info()
        {
            return array('title'     => $this->name,
                         'functions' => array($this->name => 'search'));
        }
    '''

    def private options(Application it) '''
        /**
         * Display the search form.
         *
         * @return string template output
         */
        public function options($args)
        {
            if (!SecurityUtil::checkPermission($this->name . '::', '::', ACCESS_READ)) {
                return '';
            }

            $view = Zikula_View::getInstance($this->name);

            «FOR entity : getAllEntities.filter(e|e.hasAbstractStringFieldsEntity)»
                «val fieldName = 'active_' + entity.name.formatForCode»
                $view->assign('«fieldName»', (!isset($args['«fieldName»']) || isset($args['active']['«fieldName»'])));
            «ENDFOR»

            return $view->fetch('search/options.tpl');
        }
    '''

    def private search(Application it) '''
        /**
         * Executes the actual search process.
         */
        public function search($args)
        {
            if (!SecurityUtil::checkPermission($this->name . '::', '::', ACCESS_READ)) {
                return '';
            }

            // ensure that database information of Search module is loaded
            ModUtil::dbInfoLoad('Search');

            // save session id as it is used when inserting search results below
            $sessionId  = session_id();

            // retrieve list of activated object types
            $searchTypes = isset($args['objectTypes']) ? (array)$args['objectTypes'] : (array)FormUtil::getPassedValue('search_«appName.formatForDB»_types', array(), 'GETPOST');

            $utilArgs = array('api' => 'search', 'action' => 'search');
            $allowedTypes = «appName»_Util_Controller::getObjectTypes('api', $utilArgs);
            $entityManager = ServiceUtil::getService('doctrine.entitymanager');
            $currentPage = 1;
            $resultsPerPage = 50;

            foreach ($searchTypes as $objectType) {
                if (!in_array($objectType, $allowedTypes)) {
                    continue;
                }

                $whereArray = array();
                $languageField = null;
                switch ($objectType) {
                    «FOR entity : getAllEntities.filter(e|e.hasAbstractStringFieldsEntity)»
                        case '«entity.name.formatForCode»':
                            «FOR field : entity.getAbstractStringFieldsEntity»
                                $whereArray[] = '«field.name.formatForCode»';
                            «ENDFOR»
                            «IF entity.hasLanguageFieldsEntity»
                            $languageField = '«entity.getLanguageFieldsEntity.head»';
                            «ENDIF»
                            break;
                    «ENDFOR»
                }
                $where = Search_Api_User::construct_where($args, $whereArray, $languageField);

                $repository = $entityManager->getRepository($this->name . '_Entity_' . ucfirst($objectType));
                // get objects from database
                list($entities, $objectCount) = $repository->selectWherePaginated($where, '', $currentPage, $resultsPerPage, false);

                if ($objectCount == 0) {
                    continue;
                }

                $idFields = ModUtil::apiFunc($this->name, 'selection', 'getIdFields', array('ot' => $objectType));
                $titleField = $repository->getTitleFieldName();
                $descriptionField = $repository->getDescriptionFieldName();
                «val hasUserDisplay = !getAllUserControllers.filter(e|e.hasActions('display')).isEmpty»
                foreach ($entities as $entity) {
                    «IF hasUserDisplay»
                        $urlArgs = array('ot' => $objectType);
                    «ENDIF»
                    // create identifier for permission check
                    $instanceId = '';
                    foreach ($idFields as $idField) {
                        «IF hasUserDisplay»
                            $urlArgs[$idField] = $entity[$idField];
                        «ENDIF»
                        if (!empty($instanceId)) {
                            $instanceId .= '_';
                        }
                        $instanceId .= $entity[$idField];
                    }
                    «IF hasUserDisplay»
                        $urlArgs['id'] = $instanceId;
                        if (isset($entity['slug'])) {
                            $urlArgs['slug'] = $entity['slug'];
                        }

                    «ENDIF»
                    if (!SecurityUtil::checkPermission($this->name . ':' . ucfirst($objectType) . ':', $instanceId . '::', ACCESS_OVERVIEW)) {
                        continue;
                    }

                    $title = ($titleField != '') ? $entity[$titleField] : $this->__('Item');
                    $description = ($descriptionField != '') ? $entity[$descriptionField] : '';
                    $created = (isset($entity['createdDate'])) ? $entity['createdDate'] : '';

                    $searchItem = array(
                        'title'   => $title,
                        'text'    => $description,
                        'extra'   => «IF hasUserDisplay»serialize($urlArgs)«ELSE»''«ENDIF»,
                        'created' => $created,
                        'module'  => $this->name,
                        'session' => $sessionId
                    );

                    if (!DBUtil::insertObject($searchItem, 'search_result')) {
                        return LogUtil::registerError($this->__('Error! Could not save the search results.'));
                    }
                }
            }

            return true;
        }
    '''

    def private searchCheck(Application it) '''
        /**
         * Assign URL to items.
         */
        public function search_check($args)
        {
            «val hasUserDisplay = !getAllUserControllers.filter(e|e.hasActions('display')).isEmpty»
            «IF hasUserDisplay»
                $datarow = &$args['datarow'];
                $urlArgs = unserialize($datarow['extra']);
                $datarow['url'] = ModUril::url($this->name, 'user', 'display', $urlArgs);
            «ELSE»
                // nothing to do as we have no display pages which could be linked
            «ENDIF»
            return true;
        }
    '''

    def private searchApiImpl(Application it) '''
        /**
         * Search api implementation class.
         */
        class «appName»_Api_Search extends «appName»_Api_Base_Search
        {
            // feel free to extend the search api here
        }
    '''
}