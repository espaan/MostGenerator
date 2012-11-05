package org.zikula.modulestudio.generator.cartridges.zclassic.view.plugin.form

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import org.eclipse.xtext.generator.IFileSystemAccess
import org.zikula.modulestudio.generator.cartridges.zclassic.smallstuff.FileHelper
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.Utils

class UserInput {
    @Inject extension FormattingExtensions = new FormattingExtensions()
    @Inject extension NamingExtensions = new NamingExtensions()
    @Inject extension Utils = new Utils()

    FileHelper fh = new FileHelper()

    def generate(Application it, IFileSystemAccess fsa) {
        val formPluginPath = appName.getAppSourceLibPath + 'Form/Plugin/'
        fsa.generateFile(formPluginPath + 'Base/UserInput.php', formUserInputBaseFile)
        fsa.generateFile(formPluginPath + 'UserInput.php', formUserInputFile)
        fsa.generateFile(viewPluginFilePath('function', 'UserInput'), formUserInputPluginFile)
    }

    def private formUserInputBaseFile(Application it) '''
        «fh.phpFileHeader(it)»
        «formUserInputBaseImpl»
    '''

    def private formUserInputFile(Application it) '''
        «fh.phpFileHeader(it)»
        «formUserInputImpl»
    '''

    def private formUserInputPluginFile(Application it) '''
        «fh.phpFileHeader(it)»
        «formUserInputPluginImpl»
    '''

    def private formUserInputBaseImpl(Application it) '''
        /**
         * User field plugin providing an autocomplete for user names.
         *
         * You can also use all of the features from the Zikula_Form_Plugin_TextInput plugin since
         * the user input inherits from it.
         */
        class «appName»_Form_Plugin_Base_UserInput extends Zikula_Form_Plugin_TextInput
        {
            /**
             * Get filename of this file.
             * The information is used to re-establish the plugins on postback.
             *
             * @return string
             */
            public function getFilename()
            {
                return __FILE__;
            }

            /**
             * Create event handler.
             *
             * @param Zikula_Form_View $view    Reference to Zikula_Form_View object.
             * @param array            &$params Parameters passed from the Smarty plugin function.
             *
             * @see    Zikula_Form_AbstractPlugin
             * @return void
             */
            public function create(Zikula_Form_View $view, &$params)
            {
                $params['maxLength'] = 25;

                // let parent plugin do the work in detail
                parent::create($view, $params);
            }

            /**
             * Helper method to determine css class.
             *
             * @see Zikula_Form_Plugin_TextInput
             *
             * @return string the list of css classes to apply
             */
            protected function getStyleClass()
            {
                $class = parent::getStyleClass();
                return str_replace('z-form-text', 'z-form-user', $class);
            }

            /**
             * Render event handler.
             *
             * @param Zikula_Form_View $view Reference to Zikula_Form_View object.
             *
             * @return string The rendered output
             */
            public function render(Zikula_Form_View $view)
            {
                $dom = ZLanguage::getModuleDomain('«appName»');

                //$result = parent::render($view);

                // start code from TextInput base class
                $titleHtml = ($this->toolTip != null ? ' title="' . $view->translateForDisplay($this->toolTip) . '"' : '');
                $readOnlyHtml = ($this->readOnly ? ' readonly="readonly" tabindex="-1"' : '');
                $sizeHtml = ($this->size > 0 ? " size=\"{$this->size}\"" : '');
                $maxLengthHtml = ($this->maxLength > 0 ? " maxlength=\"{$this->maxLength}\"" : '');
                $class = $this->getStyleClass();

                $attributes = $this->renderAttributes($view);
                // end code from TextInput base class

                if ($this->readOnly) {
                    return $result;
                }

                $selectorDefaultValue = '';
                if (intval($this->text) > 0) {
                    $selectorDefaultValue = UserUtil::getVar('uname', intval($this->text));
                }

                $result = "<div id=\"" . $this->getId() . "LiveSearch\" class=\"«prefix»LiveSearchUser z-hide\">
                        <img src=\"/images/icons/extrasmall/search.png\" alt=\"" . __('Search user', $dom) . "\" title=\"" . __('Search user', $dom) . "\" width=\"16\" height=\"16\" />
                        <input type=\"text\" id=\"" . $this->getId() . "Selector\" name=\"" . $this->getId() . "Selector\"{$titleHtml}{$sizeHtml}{$maxLengthHtml}{$readOnlyHtml} class=\"{$class}\" value=\"{$selectorDefaultValue}\"{$attributes} />
                        <img src=\"/images/ajax/indicator_circle.gif\" alt=\"\" id=\"" . $this->getId() . "Indicator\" style=\"display: none\" width=\"16\" height=\"16\" />
                        <div id=\"" . $this->getId() . "SelectorChoices\" class=\"«prefix»AutoCompleteUser\"></div>";

                if ($this->mandatory && $this->mandatorysym) {
                    $result .= '<span class="z-form-mandatory-flag">*</span>';
                }

                $result .= "</div>";
                $result .= "<noscript><p>" . __('This function requires JavaScript activated!', $dom) . "</p></noscript>";
                $result .= "<input type=\"hidden\" id=\"" . $this->getId() . "\" name=\"" . $this->getId() . "\" value=\"" . DataUtil::formatForDisplay($this->text) . "\" />";

                return $result;
            }

            /**
             * Parses a value.
             *
             * @param Zikula_Form_View $view Reference to Zikula_Form_View object.
             * @param string           $text Text.
             *
             * @return string Parsed Text.
             */
            public function parseValue(Zikula_Form_View $view, $text)
            {
                if (empty($text)) {
                    return 0;//null;
                }

                return $text;
            }

            /**
             * Validates the input string.
             *
             * @param Zikula_Form_View $view Reference to Zikula_Form_View object.
             *
             * @return boolean
             */
            public function validate(Zikula_Form_View $view)
            {
                parent::validate($view);

                if (!$this->isValid) {
                    return;
                }

                if (strlen($this->text) > 0) {
                    $uid = intval($this->text);
                    if (UserUtil::getVar('uname', $uid) == null) {
                        $this->setError(__('Error! Invalid user.'));
                        return false;
                    }
                }
            }
        }
    '''

    def private formUserInputImpl(Application it) '''
        /**
         * User field plugin providing an autocomplete for user names.
         *
         * You can also use all of the features from the Zikula_Form_Plugin_TextInput plugin since
         * the user input inherits from it.
         */
        class «appName»_Form_Plugin_UserInput extends «appName»_Form_Plugin_Base_UserInput
        {
            // feel free to add your customisation here
        }
    '''

    def private formUserInputPluginImpl(Application it) '''
        /**
         * The «appName.formatForDB»UserInput plugin handles fields carrying user ids.
         * It provides an autocomplete for user names.
         *
         * @param  array            $params  All attributes passed to this function from the template.
         * @param  Zikula_Form_View $view    Reference to the view object.
         *
         * @return string The output of the plugin.
         */
        function smarty_function_«appName.formatForDB»UserInput($params, $view)
        {
            return $view->registerPlugin('«appName»_Form_Plugin_UserInput', $params);
        }
    '''
}