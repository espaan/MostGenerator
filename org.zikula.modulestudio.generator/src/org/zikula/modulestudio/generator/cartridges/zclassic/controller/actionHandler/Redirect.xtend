package org.zikula.modulestudio.generator.cartridges.zclassic.controller.actionHandler

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import de.guite.modulestudio.metamodel.modulestudio.Controller
import de.guite.modulestudio.metamodel.modulestudio.Entity
import de.guite.modulestudio.metamodel.modulestudio.EntityTreeType
import org.zikula.modulestudio.generator.extensions.ControllerExtensions
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.ModelJoinExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.UrlExtensions

/**
 * Redirect processing functions for edit form handlers.
 */
class Redirect {
    @Inject extension ControllerExtensions = new ControllerExtensions()
    @Inject extension FormattingExtensions = new FormattingExtensions()
    @Inject extension ModelJoinExtensions = new ModelJoinExtensions()
    @Inject extension NamingExtensions = new NamingExtensions()
    @Inject extension UrlExtensions = new UrlExtensions()

    def getRedirectCodes(Controller it, Application app, String actionName) '''
        /**
         * Get list of allowed redirect codes.
         */
        protected function getRedirectCodes()
        {
            $codes = array();
            «FOR someController : app.getAllControllers»
                «val controllerName = someController.formattedName»
                «IF someController.hasActions('main')»
                    // main page of «controllerName» area
                    $codes[] = '«controllerName»';
                «ENDIF»
                «IF someController.hasActions('view')»
                    // «controllerName» list of entities
                    $codes[] = '«controllerName»View';
                «ENDIF»
                «IF someController.hasActions('display')»
                    // «controllerName» display page of treated entity
                    $codes[] = '«controllerName»Display';
                «ENDIF»
            «ENDFOR»
            return $codes;
        }
    '''

    def getRedirectCodes(Entity it, Application app, Controller controller, String actionName) '''
        /**
         * Get list of allowed redirect codes.
         */
        protected function getRedirectCodes()
        {
            $codes = parent::getRedirectCodes();
            «FOR incomingRelation : getIncomingJoinRelationsWithOneSource»
                «val sourceEntity = incomingRelation.source»
                «IF sourceEntity.name != it.name»
                    «FOR someController : app.getAllControllers»
                        «val controllerName = someController.formattedName»
                        «IF someController.hasActions('view')»
                            // «controllerName» list of «sourceEntity.nameMultiple.formatForDisplay»
                            $codes[] = '«controllerName»View«sourceEntity.name.formatForCodeCapital»';
                        «ENDIF»
                        «IF someController.hasActions('display')»
                            // «controllerName» display page of treated «sourceEntity.name.formatForDisplay»
                            $codes[] = '«controllerName»Display«sourceEntity.name.formatForCodeCapital»';
                        «ENDIF»
                    «ENDFOR»
                «ENDIF»
            «ENDFOR»
            return $codes;
        }
    '''

    def getDefaultReturnUrl(Entity it, Application app, Controller controller, String actionName) '''
        /**
         * Get the default redirect url. Required if no returnTo parameter has been supplied.
         * This method is called in handleCommand so we know which command has been performed.
         */
        protected function getDefaultReturnUrl($args, $obj)
        {
            «IF controller.hasActions('view')»
                // redirect to the list of «nameMultiple.formatForCode»
                $viewArgs = array('ot' => $this->objectType);
                «IF tree != EntityTreeType::NONE»
                    $viewArgs['tpl'] = 'tree';
                «ENDIF»
                $url = ModUtil::url($this->name, '«controller.formattedName»', 'view', $viewArgs);
            «ELSEIF controller.hasActions('main')»
                // redirect to the main page
                $url = ModUtil::url($this->name, '«controller.formattedName»', 'main');
            «ELSE»
                $url = System::getHomepageUrl();
            «ENDIF»

            «IF controller.hasActions('display')»
                «IF tree != EntityTreeType::NONE»
                    /*
                «ENDIF»
                if ($args['commandName'] != 'delete' && !($this->mode == 'create' && $args['commandName'] == 'cancel')) {
                    // redirect to the detail page of treated «name.formatForCode»
                    $url = ModUtil::url($this->name, '«controller.formattedName»', «modUrlDisplay('this->idValues', false)»);
                }
                «IF tree != EntityTreeType::NONE»
                    */
                «ENDIF»
            «ENDIF»
            return $url;
        }
    '''

    def getRedirectUrl(Entity it, Application app, Controller controller, String actionName) '''
        /**
         * Get url to redirect to.
         */
        protected function getRedirectUrl($args, $obj)
        {
            if ($this->inlineUsage == true) {
                $urlArgs = array('idp' => $this->idPrefix,
                                 'com' => $args['commandName']);
                $urlArgs = $this->addIdentifiersToUrlArgs($urlArgs);
                // inline usage, return to special function for closing the Zikula.UI.Window instance
                return ModUtil::url($this->name, '«controller.formattedName»', 'handleInlineRedirect', $urlArgs);
            }

            if ($this->repeatCreateAction) {
                return $this->repeatReturnUrl;
            }

            // normal usage, compute return url from given redirect code
            if (!in_array($this->returnTo, $this->getRedirectCodes())) {
                // invalid return code, so return the default url
                return $this->getDefaultReturnUrl($args, $obj);
            }

            // parse given redirect code and return corresponding url
            switch ($this->returnTo) {
                «FOR someController : app.getAllControllers.filter(e|!e.isAjaxController)»
                    «val controllerName = someController.formattedName»
                    «IF someController.hasActions('main')»
                        case '«controllerName»':
                                    return ModUtil::url($this->name, '«controllerName»', 'main');
                    «ENDIF»
                    «IF someController.hasActions('view')»
                        case '«controllerName»View':
                                    return ModUtil::url($this->name, '«controllerName»', 'view',
                                                             array('ot' => $this->objectType));
                    «ENDIF»
                    «IF someController.hasActions('display')»
                        case '«controllerName»Display':
                                    if ($args['commandName'] != 'delete' && !($this->mode == 'create' && $args['commandName'] == 'cancel')) {
                                        return ModUtil::url($this->name, '«controllerName»', $this->addIdentifiersToUrlArgs());
                                    }
                                    return $this->getDefaultReturnUrl($args, $obj);
                    «ENDIF»
                «ENDFOR»
                «FOR incomingRelation : getIncomingJoinRelationsWithOneSource»
                    «val sourceEntity = incomingRelation.source»
                    «IF sourceEntity.name != it.name»
                        «FOR someController : app.getAllControllers.filter(e|!e.isAjaxController)»
                            «val controllerName = someController.formattedName»
                            «IF someController.hasActions('view')»
                                case '«controllerName»View«sourceEntity.name.formatForCodeCapital»':
                                    return ModUtil::url($this->name, '«controllerName»', 'view',
                                                             array('ot' => '«sourceEntity.name.formatForCode»'));
                            «ENDIF»
                            «IF someController.hasActions('display')»
                                case '«controllerName»Display«sourceEntity.name.formatForCodeCapital»':
                                    if (!empty($this->«incomingRelation.getRelationAliasName(false)»)) {
                                        return ModUtil::url($this->name, '«controllerName»', 'display', array('ot' => '«sourceEntity.name.formatForCode»', 'id' => $this->«incomingRelation.getRelationAliasName(false)»));
                                    }
                                    return $this->getDefaultReturnUrl($args, $obj);
                            «ENDIF»
                        «ENDFOR»
                    «ENDIF»
                «ENDFOR»
                        default:
                                    return $this->getDefaultReturnUrl($args, $obj);
            }
        }
    '''
}