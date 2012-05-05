package org.zikula.modulestudio.generator.cartridges.zclassic.controller.apis

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import org.eclipse.xtext.generator.IFileSystemAccess
import org.zikula.modulestudio.generator.cartridges.zclassic.smallstuff.FileHelper
import org.zikula.modulestudio.generator.cartridges.zclassic.view.additions.BlocksView
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.ModelExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.Utils

class Blocks {
    @Inject extension FormattingExtensions = new FormattingExtensions()
    @Inject extension ModelExtensions = new ModelExtensions()
    @Inject extension NamingExtensions = new NamingExtensions()
    @Inject extension Utils = new Utils()

    FileHelper fh = new FileHelper()

    def generate(Application it, IFileSystemAccess fsa) {
        val blockPath = appName.getAppSourceLibPath + 'Block/'
        fsa.generateFile(blockPath + 'Base/ItemList.php', listBlockBaseFile)
        fsa.generateFile(blockPath + 'ItemList.php', listBlockFile)
        new BlocksView().generate(it, fsa)
    }

    def private listBlockBaseFile(Application it) '''
    	«fh.phpFileHeader(it)»
    	«listBlockBaseClass»
    '''

    def private listBlockFile(Application it) '''
    	«fh.phpFileHeader(it)»
    	«listBlockImpl»
    '''

    def private listBlockBaseClass(Application it) '''
		/**
		 * Generic item list block base class.
		 */
		class «appName»_Block_Base_ItemList extends Zikula_Controller_AbstractBlock
		{
		    «listBlockBaseImpl»
		}
    '''

    def private listBlockBaseImpl(Application it) '''
        /**
         * Initialise the block
         */
        public function init()
        {
            SecurityUtil::registerPermissionSchema('«appName»:ItemListBlock:', 'Block title::');
        }

        /**
         * Get information on the block
         *
         * @return       array       The block information
         */
        public function info()
        {
            $requirementMessage = '';
            // check if the module is available at all
            if (!ModUtil::available('«appName»')) {
                $requirementMessage .= $this->__('Notice: This block will not be displayed until you activate the «appName» module.');
            }

            return array('module'           => '«appName»',
                         'text_type'        => $this->__('Item list'),
                         'text_type_long'   => $this->__('Show a list of «appName» items based on different criteria.'),
                         'allow_multiple'   => true,
                         'form_content'     => false,
                         'form_refresh'     => false,
                         'show_preview'     => true,
                         'admin_tableless'  => true,
                         'requirement'      => $requirementMessage);
        }

        /**
         * Display the block
         *
         * @param        array       $blockinfo a blockinfo structure
         * @return       output      the rendered block
         */
        public function display($blockinfo)
        {
            // only show block content if the user has the required permissions
            if (!SecurityUtil::checkPermission('«appName»:ItemListBlock:', "$blockinfo[title]::", ACCESS_OVERVIEW)) {
                return false;
            }

            // check if the module is available at all
            if (!ModUtil::available('«appName»')) {
                return false;
            }

            // get current block content
            $vars = BlockUtil::varsFromContent($blockinfo['content']);
            $vars['bid'] = $blockinfo['bid'];

            // set default values for all params which are not properly set
            if (!isset($vars['objectType']) || empty($vars['objectType'])) {
                $vars['objectType'] = '«getLeadingEntity.name.formatForCode»';
            }
            if (!isset($vars['sorting']) || empty($vars['sorting'])) {
                $vars['sorting'] = 'default';
            }
            if (!isset($vars['amount']) || !is_numeric($vars['amount'])) {
                $vars['amount'] = 5;
            }
            if (!isset($vars['template'])) {
                $vars['template'] = 'itemlist_' . ucwords($vars['objectType']) . '_display.tpl';
            }
            if (!isset($vars['filter'])) {
                $vars['filter'] = '';
            }

            ModUtil::initOOModule('«appName»');

            if (!isset($vars['objectType']) || !in_array($vars['objectType'], «appName»_Util_Controller::getObjectTypes('block'))) {
                $vars['objectType'] = «appName»_Util_Controller::getDefaultObjectType('block');
            }

            $objectType = $vars['objectType'];

            $serviceManager = ServiceUtil::getManager();
            $entityManager = $serviceManager->getService('doctrine.entitymanager');
            $repository = $entityManager->getRepository('«appName»_Entity_' . ucfirst($objectType));

            $idFields = ModUtil::apiFunc('«appName»', 'selection', 'getIdFields', array('ot' => $objectType));

            $sortParam = '';
            if ($vars['sorting'] == 'random') {
                $sortParam = 'RAND()';
            } elseif ($vars['sorting'] == 'newest') {
                if (count($idFields) == 1) {
                    $sortParam = $idFields[0] . ' DESC';
                } else {
                    foreach ($idFields as $idField) {
                        if (!empty($sortParam)) {
                            $sortParam .= ', ';
                        }
                        $sortParam .= $idField . ' ASC';
                    }
                }
            } elseif ($vars['sorting'] == 'default') {
                $sortParam = $repository->getDefaultSortingField() . ' ASC';
            }

            // get objects from database
            $selectionArgs = array(
                'ot' => $objectType,
                'where' => $vars['filter'],
                'orderBy' => $sortParam,
                'currentPage' => 1,
                'resultsPerPage' => $vars['amount']
            );
            list($entities, $objectCount) = ModUtil::apiFunc('«appName»', 'selection', 'getEntitiesPaginated', $selectionArgs);

            $this->view->setCaching(true);

            // assign block vars and fetched data
            $this->view->assign('vars', $vars)
                       ->assign('objectType', $objectType)
                       ->assign('items', $entities)
                       ->assign($repository->getAdditionalTemplateParameters('block'));

            // set a block title
            if (empty($blockinfo['title'])) {
                $blockinfo['title'] = $this->__('«appName» items');
            }

            $output = '';
            $templateForObjectType = str_replace('itemlist_', 'itemlist_' . ucwords($objectType) . '_', $vars['template']);
            if ($this->view->template_exists('contenttype/' . $templateForObjectType)) {
                $output = $this->view->fetch('contenttype/' . $templateForObjectType);
            } elseif ($this->view->template_exists('contenttype/' . $vars['template'])) {
                $output = $this->view->fetch('contenttype/' . $vars['template']);
            } elseif ($this->view->template_exists('block/' . $templateForObjectType)) {
                $output = $this->view->fetch('block/' . $templateForObjectType);
            } elseif ($this->view->template_exists('block/' . $vars['template'])) {
            $output = $this->view->fetch('block/' . $vars['template']);
            } else {
                $output = $this->view->fetch('block/itemlist.tpl');
            }

            $blockinfo['content'] = $output;

            // return the block to the theme
            return BlockUtil::themeBlock($blockinfo);
        }

        /**
         * Modify block settings
         *
         * @param        array       $blockinfo a blockinfo structure
         * @return       output      the block form
         */
        public function modify($blockinfo)
        {
            // Get current content
            $vars = BlockUtil::varsFromContent($blockinfo['content']);

            // set default values for all params which are not properly set
            if (!isset($vars['objectType']) || empty($vars['objectType'])) {
                $vars['objectType'] = '«getLeadingEntity.name.formatForCode»';
            }
            if (!isset($vars['sorting']) || empty($vars['sorting'])) {
                $vars['sorting'] = 'default';
            }
            if (!isset($vars['amount']) || !is_numeric($vars['amount'])) {
                $vars['amount'] = 5;
            }
            if (!isset($vars['template'])) {
                $vars['template'] = 'itemlist_' . $vars['objectType'] . '_display.tpl';
            }
            if (!isset($vars['filter'])) {
                $vars['filter'] = '';
            }

            $this->view->setCaching(false);

            // assign the approriate values
            $this->view->assign($vars);

            // clear the block cache
            $this->view->clear_cache('block/itemlist_display.tpl');
            $this->view->clear_cache('block/itemlist_' . ucwords($vars['objectType']) . '_display.tpl');
            $this->view->clear_cache('block/itemlist_display_description.tpl');
            $this->view->clear_cache('block/itemlist_' . ucwords($vars['objectType']) . '_display_description.tpl');

            // Return the output that has been generated by this function
            return $this->view->fetch('block/itemlist_modify.tpl');
        }

        /**
         * Update block settings
         *
         * @param        array       $blockinfo a blockinfo structure
         * @return       $blockinfo  the modified blockinfo structure
         */
        public function update($blockinfo)
        {
            // Get current content
            $vars = BlockUtil::varsFromContent($blockinfo['content']);

            $vars['objectType'] = $this->request->request->filter('objecttype', '«getLeadingEntity.name.formatForCode»', FILTER_SANITIZE_STRING);
            $vars['sorting'] = $this->request->request->filter('sorting', 'default', FILTER_SANITIZE_STRING);
            $vars['amount'] = (int) $this->request->request->filter('amount', 5, FILTER_VALIDATE_INT);
            $vars['template'] = $this->request->request->get('template', '');
            $vars['filter'] = $this->request->request->get('filter', '');

            if (!in_array($vars['objectType'], «appName»_Util_Controller::getObjectTypes('block'))) {
                $vars['objectType'] = «appName»_Util_Controller::getDefaultObjectType('block');
            }

            // write back the new contents
            $blockinfo['content'] = BlockUtil::varsToContent($vars);

            // clear the block cache
            $this->view->clear_cache('block/itemlist_display.tpl');
            $this->view->clear_cache('block/itemlist_' . ucwords($vars['objectType']) . '_display.tpl');
            $this->view->clear_cache('block/itemlist_display_description.tpl');
            $this->view->clear_cache('block/itemlist_' . ucwords($vars['objectType']) . '_display_description.tpl');

            return $blockinfo;
        }
    '''

    def private listBlockImpl(Application it) '''
        /**
         * Generic item list block implementation class.
         */
        class «appName»_Block_ItemList extends «appName»_Block_Base_ItemList
        {
            // feel free to extend the item list block here
        }
    '''
}