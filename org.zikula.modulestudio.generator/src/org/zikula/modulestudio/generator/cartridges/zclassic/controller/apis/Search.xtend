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
    @Inject extension ControllerExtensions = new ControllerExtensions
    @Inject extension FormattingExtensions = new FormattingExtensions
    @Inject extension ModelExtensions = new ModelExtensions
    @Inject extension NamingExtensions = new NamingExtensions
    @Inject extension Utils = new Utils

    FileHelper fh = new FileHelper

    def generate(Application it, IFileSystemAccess fsa) {
        val apiPath = getAppSourceLibPath + 'Api/'
        val apiClassSuffix = if (!targets('1.3.5')) 'Api' else ''
        val apiFileName = 'Search' + apiClassSuffix + '.php'
        fsa.generateFile(apiPath + 'Base/' + apiFileName, searchApiBaseFile)
        fsa.generateFile(apiPath + apiFileName, searchApiFile)
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
        «IF !targets('1.3.5')»
            namespace «appNamespace»\Api\Base;

            use «appNamespace»\Util\ControllerUtil;

            use FormUtil;
            use LogUtil;
            use ModUtil;
            use SecurityUtil;
            use ServiceUtil;
            use Zikula_AbstractApi;
            use Zikula_View;

            use Zikula\Module\SearchModule\Entity\SearchResultEntity;

        «ENDIF»
        /**
         * Search api base class.
         */
        class «IF targets('1.3.5')»«appName»_Api_Base_Search«ELSE»SearchApi«ENDIF» extends Zikula_AbstractApi
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
         * @param array $args List of arguments.
         *
         * @return string template output
         */
        public function options(array $args = array())
        {
            if (!SecurityUtil::checkPermission($this->name . '::', '::', ACCESS_READ)) {
                return '';
            }

            $view = Zikula_View::getInstance($this->name);

            «FOR entity : getAllEntities.filter(e|e.hasAbstractStringFieldsEntity)»
                «val fieldName = 'active_' + entity.name.formatForCode»
                $view->assign('«fieldName»', (!isset($args['«fieldName»']) || isset($args['active']['«fieldName»'])));
            «ENDFOR»

            return $view->fetch('«IF targets('1.3.5')»search«ELSE»Search«ENDIF»/options.tpl');
        }
    '''

    def private search(Application it) '''
        /**
         * Executes the actual search process.
         *
         * @param array $args List of arguments.
         *
         * @return boolean
         */
        public function search(array $args = array())
        {
            if (!SecurityUtil::checkPermission($this->name . '::', '::', ACCESS_READ)) {
                return '';
            }

            // ensure that database information of Search module is loaded
            ModUtil::dbInfoLoad('Search');

            // save session id as it is used when inserting search results below
            $sessionId  = session_id();

            // retrieve list of activated object types
            $searchTypes = isset($args['objectTypes']) ? (array)$args['objectTypes'] : (array) FormUtil::getPassedValue('search_«appName.formatForDB»_types', array(), 'GETPOST');

            $controllerHelper = new «IF targets('1.3.5')»«appName»_Util_Controller«ELSE»ControllerUtil«ENDIF»($this->serviceManager«IF !targets('1.3.5')», ModUtil::getModule($this->name)«ENDIF»);
            $utilArgs = array('api' => 'search', 'action' => 'search');
            $allowedTypes = $controllerHelper->getObjectTypes('api', $utilArgs);
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
                                $whereArray[] = 'tbl.«field.name.formatForCode»';
                            «ENDFOR»
                            «IF entity.hasLanguageFieldsEntity»
                            $languageField = '«entity.getLanguageFieldsEntity.head»';
                            «ENDIF»
                            break;
                    «ENDFOR»
                }
                $where = «IF targets('1.3.5')»Search_Api_User«ELSE»\Zikula\Module\Search\Api\UserApi«ENDIF»::construct_where($args, $whereArray, $languageField);

                «IF targets('1.3.5')»
                    $entityClass = $this->name . '_Entity_' . ucwords($objectType);
                «ELSE»
                    $entityClass = '\\«vendor.formatForCodeCapital»\\«name.formatForCodeCapital»Module\\Entity\\' . ucwords($objectType) . 'Entity';
                «ENDIF»
                $repository = $entityManager->getRepository($entityClass);

                // get objects from database
                list($entities, $objectCount) = $repository->selectWherePaginated($where, '', $currentPage, $resultsPerPage, false);

                if ($objectCount == 0) {
                    continue;
                }

                $idFields = ModUtil::apiFunc($this->name, 'selection', 'getIdFields', array('ot' => $objectType));
                $titleField = $repository->getTitleFieldName();
                $descriptionField = $repository->getDescriptionFieldName();
                «val hasUserDisplay = !getAllUserControllers.filter(e|e.hasActions('display')).empty»
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
                        /* commented out as it could exceed the maximum length of the 'extra' field
                        if (isset($entity['slug'])) {
                            $urlArgs['slug'] = $entity['slug'];
                        }*/

                    «ENDIF»
                    if (!SecurityUtil::checkPermission($this->name . ':' . ucfirst($objectType) . ':', $instanceId . '::', ACCESS_OVERVIEW)) {
                        continue;
                    }

                    $title = ($titleField != '') ? $entity[$titleField] : $this->__('Item');
                    $description = ($descriptionField != '') ? $entity[$descriptionField] : '';
                    $created = (isset($entity['createdDate'])) ? $entity['createdDate']->format('Y-m-d H:i:s') : '';

                    $searchItemData = array(
                        'title'   => $title,
                        'text'    => $description,
                        'extra'   => «IF hasUserDisplay»serialize($urlArgs)«ELSE»''«ENDIF»,
                        'created' => $created,
                        'module'  => $this->name,
                        'session' => $sessionId
                    );

                    «IF targets('1.3.5')»
                    if (!DBUtil::insertObject($searchItemData, 'search_result')) {
                        return LogUtil::registerError($this->__('Error! Could not save the search results.'));
                    }
                    «ELSE»
                    $searchItem = new SearchResultEntity();
                    foreach ($searchItemData as $k => $v) {
                        $fieldName = ($k == 'session') ? 'sesid' : $k;
                        $searchItem[$fieldName] = $v;
                    }
                    try {
                        $this->entityManager->persist($searchItem);
                        $this->entityManager->flush();
                    } catch (\Exception $e) {
                        return LogUtil::registerError($this->__('Error! Could not save the search results.'));
                    }
                    «ENDIF»
                }
            }

            return true;
        }
    '''

    def private searchCheck(Application it) '''
        /**
         * Assign URL to items.
         *
         * @param array $args List of arguments.
         *
         * @return boolean
         */
        public function search_check(array $args = array())
        {
            «val hasUserDisplay = !getAllUserControllers.filter(e|e.hasActions('display')).empty»
            «IF hasUserDisplay»
                $datarow = &$args['datarow'];
                $urlArgs = unserialize($datarow['extra']);
                $datarow['url'] = ModUtil::url($this->name, 'user', 'display', $urlArgs);
            «ELSE»
                // nothing to do as we have no display pages which could be linked
            «ENDIF»
            return true;
        }
    '''

    def private searchApiImpl(Application it) '''
        «IF !targets('1.3.5')»
            namespace «appNamespace»\Api;

            use «appNamespace»\Api\Base\SearchApi as BaseSearchApi;

        «ENDIF»
        /**
         * Search api implementation class.
         */
        «IF targets('1.3.5')»
        class «appName»_Api_Search extends «appName»_Api_Base_Search
        «ELSE»
        class SearchApi extends BaseSearchApi
        «ENDIF»
        {
            // feel free to extend the search api here
        }
    '''
}
