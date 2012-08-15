package org.zikula.modulestudio.generator.cartridges.zclassic.controller.listener

import de.guite.modulestudio.metamodel.modulestudio.Application

class View {

    def generate(Application it, Boolean isBase) '''
        /**
         * Listener for the `view.init` event.
         *
         * Occurs just before `Zikula_View#__construct()` finishes.
         * The subject is the Zikula_View instance.
         *
         * @param Zikula_Event $event The event instance.
         */
        public static function init(Zikula_Event $event)
        {
            «IF !isBase»
                parent::init($event);
            «ENDIF»
        }

        /**
         * Listener for the `view.postfetch` event.
         *
         * Filter of result of a fetch.
         * Receives `Zikula_View` instance as subject,
         * args are `array('template' => $template)`,
         * $data was the result of the fetch to be filtered.
         *
         * @param Zikula_Event $event The event instance.
         */
        public static function postFetch(Zikula_Event $event)
        {
            «IF !isBase»
                parent::postFetch($event);
            «ENDIF»
        }
    '''
}
