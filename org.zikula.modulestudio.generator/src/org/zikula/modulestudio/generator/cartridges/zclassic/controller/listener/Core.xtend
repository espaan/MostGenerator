package org.zikula.modulestudio.generator.cartridges.zclassic.controller.listener

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import org.zikula.modulestudio.generator.extensions.Utils

class Core {
    @Inject extension Utils = new Utils()

    def generate(Application it, Boolean isBase) '''
        /**
         * Listener for the `api.method_not_found` event.
         *
         * Called in instances of Zikula_Api from __call().
         * Receives arguments from __call($method, argument) as $args.
         *     $event['method'] is the method which didn't exist in the main class.
         *     $event['args'] is the arguments that were passed.
         * The event subject is the class where the method was not found.
         * Must exit if $event['method'] does not match whatever the handler expects.
         * Modify $event->data and $event->stop«IF !targets('1.3.5')»Propagation«ENDIF»().
         *
         * @param «IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event The event instance.
         */
        public static function apiMethodNotFound(«IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event)
        {
            «IF !isBase»
                parent::apiMethodNotFound($event);
            «ENDIF»
        }

        /**
         * Listener for the `core.preinit` event.
         *
         * Occurs after the config.php is loaded.
         *
         * @param «IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event The event instance.
         */
        public static function preInit(«IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event)
        {
            «IF !isBase»
                parent::preInit($event);
            «ENDIF»
        }

        /**
         * Listener for the `core.init` event.
         *
         * Occurs after each `System::init()` stage, `$event['stage']` contains the stage.
         * To check if the handler should execute, do `if($event['stage'] & System::CORE_STAGES_*)`.
         *
         * @param «IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event The event instance.
         */
        public static function init(«IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event)
        {
            «IF !isBase»
                parent::init($event);
            «ENDIF»
        }

        /**
         * Listener for the `core.postinit` event.
         *
         * Occurs just before System::init() exits from normal execution.
         *
         * @param «IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event The event instance.
         */
        public static function postInit(«IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event)
        {
            «IF !isBase»
                parent::postInit($event);
            «ENDIF»
        }

        /**
         * Listener for the `controller.method_not_found` event.
         *
         * Called in instances of `Zikula_Controller` from `__call()`.
         * Receives arguments from `__call($method, argument)` as `$args`.
         *    `$event['method']` is the method which didn't exist in the main class.
         *    `$event['args']` is the arguments that were passed.
         * The event subject is the class where the method was not found.
         * Must exit if `$event['method']` does not match whatever the handler expects.
         * Modify `$event->data` and `$event->stop«IF !targets('1.3.5')»Propagation«ENDIF»()`.
         *
         * @param «IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event The event instance.
         */
        public static function controllerMethodNotFound(«IF targets('1.3.5')»Zikula_Event«ELSE»GenericEvent«ENDIF» $event)
        {
            «IF !isBase»
                parent::controllerMethodNotFound($event);

            «ENDIF»
            // You can have multiple of these methods.
            // See system/Extensions/«IF targets('1.3.5')»lib/Extensions/«ENDIF»HookUI.php for an example.
        }
    '''
}
