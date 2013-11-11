package org.zikula.modulestudio.generator.cartridges.zclassic.view.additions

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import org.eclipse.xtext.generator.IFileSystemAccess
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.GeneratorSettingsExtensions
import org.zikula.modulestudio.generator.extensions.ModelExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.Utils

class BlocksView {
    @Inject extension FormattingExtensions = new FormattingExtensions
    @Inject extension GeneratorSettingsExtensions = new GeneratorSettingsExtensions
    @Inject extension ModelExtensions = new ModelExtensions
    @Inject extension NamingExtensions = new NamingExtensions
    @Inject extension Utils = new Utils

    def generate(Application it, IFileSystemAccess fsa) {
        val templatePath = getViewPath + (if (targets('1.3.5')) 'block' else 'Block') + '/'
        if (!shouldBeSkipped(templatePath + 'itemlist.tpl')) {
            fsa.generateFile(templatePath + 'itemlist.tpl', displayTemplate)
        }
        if (!shouldBeSkipped(templatePath + 'itemlist_modify.tpl')) {
            fsa.generateFile(templatePath + 'itemlist_modify.tpl', editTemplate)
        }
    }

    def private displayTemplate(Application it) '''
        {* Purpose of this template: Display items within a block (fallback template) *}
        Default block for generic item list.
    '''

    def private editTemplate(Application it) '''
        {* Purpose of this template: Edit block for generic item list *}
        <div class="«IF targets('1.3.5')»z-formrow«ELSE»form-group«ENDIF»">
            <label for="«appName.toFirstLower»ObjectType"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{gt text='Object type'}:</label>
            «IF !targets('1.3.5')»
                <div class="col-lg-9">
            «ENDIF»
                <select id="«appName.toFirstLower»ObjectType" name="objecttype" size="1"«IF !targets('1.3.5')» class="form-control"«ENDIF»>
                    «FOR entity : getAllEntities»
                        <option value="«entity.name.formatForCode»"{if $objectType eq '«entity.name.formatForCode»'} selected="selected"{/if}>{gt text='«entity.nameMultiple.formatForDisplayCapital»'}</option>
                    «ENDFOR»
                </select>
                <span class="«IF targets('1.3.5')»z-sub z-formnote«ELSE»help-block«ENDIF»">{gt text='If you change this please save the block once to reload the parameters below.'}</span>
            «IF !targets('1.3.5')»
                </div>
            «ENDIF»
        </div>

        {if $properties ne null && is_array($properties)}
            {gt text='All' assign='lblDefault'}
            {nocache}
            {foreach key='propertyName' item='propertyId' from=$properties}
                <div class="«IF targets('1.3.5')»z-formrow«ELSE»form-group«ENDIF»">
                    {modapifunc modname='«appName»' type='category' func='hasMultipleSelection' ot=$objectType registry=$propertyName assign='hasMultiSelection'}
                    {gt text='Category' assign='categoryLabel'}
                    {assign var='categorySelectorId' value='catid'}
                    {assign var='categorySelectorName' value='catid'}
                    {assign var='categorySelectorSize' value='1'}
                    {if $hasMultiSelection eq true}
                        {gt text='Categories' assign='categoryLabel'}
                        {assign var='categorySelectorName' value='catids'}
                        {assign var='categorySelectorId' value='catids__'}
                        {assign var='categorySelectorSize' value='8'}
                    {/if}
                    <label for="{$categorySelectorId}{$propertyName}"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{$categoryLabel}</label>
                    «IF !targets('1.3.5')»
                        <div class="col-lg-9">
                    «ELSE»
                        &nbsp;
                    «ENDIF»
                        {selector_category name="`$categorySelectorName``$propertyName`" field='id' selectedValue=$catIds.$propertyName categoryRegistryModule='«appName»' categoryRegistryTable=$objectType categoryRegistryProperty=$propertyName defaultText=$lblDefault editLink=false multipleSize=$categorySelectorSize«IF !targets('1.3.5')» cssClass='form-control'«ENDIF»}
                        <span class="«IF targets('1.3.5')»z-sub z-formnote«ELSE»help-block«ENDIF»">{gt text='This is an optional filter.'}</span>
                    «IF !targets('1.3.5')»
                        </div>
                    «ENDIF»
                </div>
            {/foreach}
            {/nocache}
        {/if}

        <div class="«IF targets('1.3.5')»z-formrow«ELSE»form-group«ENDIF»">
            <label for="«appName.toFirstLower»Sorting"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{gt text='Sorting'}:</label>
            «IF !targets('1.3.5')»
                <div class="col-lg-9">
            «ENDIF»
                <select id="«appName.toFirstLower»Sorting" name="sorting"«IF !targets('1.3.5')» class="form-control"«ENDIF»>
                    <option value="random"{if $sorting eq 'random'} selected="selected"{/if}>{gt text='Random'}</option>
                    <option value="newest"{if $sorting eq 'newest'} selected="selected"{/if}>{gt text='Newest'}</option>
                    <option value="alpha"{if $sorting eq 'default' || ($sorting != 'random' && $sorting != 'newest')} selected="selected"{/if}>{gt text='Default'}</option>
                </select>
            «IF !targets('1.3.5')»
                </div>
            «ENDIF»
        </div>

        <div class="«IF targets('1.3.5')»z-formrow«ELSE»form-group«ENDIF»">
            <label for="«appName.toFirstLower»Amount"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{gt text='Amount'}:</label>
            «IF !targets('1.3.5')»
                <div class="col-lg-9">
            «ENDIF»
                <input type="text" id="«appName.toFirstLower»Amount" name="amount" maxlength="2" size="10" value="{$amount|default:"5"}"«IF !targets('1.3.5')» class="form-control"«ENDIF» />
            «IF !targets('1.3.5')»
                </div>
            «ENDIF»
        </div>

        <div class="«IF targets('1.3.5')»z-formrow«ELSE»form-group«ENDIF»">
            <label for="«appName.toFirstLower»Template"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{gt text='Template'}:</label>
            «IF !targets('1.3.5')»
                <div class="col-lg-9">
            «ENDIF»
                <select id="«appName.toFirstLower»Template" name="template"«IF !targets('1.3.5')» class="form-control"«ENDIF»>
                    <option value="itemlist_display.tpl"{if $template eq 'itemlist_display.tpl'} selected="selected"{/if}>{gt text='Only item titles'}</option>
                    <option value="itemlist_display_description.tpl"{if $template eq 'itemlist_display_description.tpl'} selected="selected"{/if}>{gt text='With description'}</option>
                    <option value="custom"{if $template eq 'custom'} selected="selected"{/if}>{gt text='Custom template'}</option>
                </select>
            «IF !targets('1.3.5')»
                </div>
            «ENDIF»
        </div>

        <div id="customTemplateArea" class="«IF targets('1.3.5')»z-formrow z-hide«ELSE»form-group hide«ENDIF»">
            <label for="«appName.toFirstLower»CustomTemplate"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{gt text='Custom template'}:</label>
            «IF !targets('1.3.5')»
                <div class="col-lg-9">
            «ENDIF»
                <input type="text" id="«appName.toFirstLower»CustomTemplate" name="customtemplate" size="40" maxlength="80" value="{$customTemplate|default:''}"«IF !targets('1.3.5')» class="form-control"«ENDIF» />
                <span class="«IF targets('1.3.5')»z-sub z-formnote«ELSE»help-block«ENDIF»">{gt text='Example'}: <em>itemlist_{$objecttype}_display.tpl</em></span>
            «IF !targets('1.3.5')»
                </div>
            «ENDIF»
        </div>

        <div class="«IF targets('1.3.5')»z-formrow z-hide«ELSE»form-group«ENDIF»">
            <label for="«appName.toFirstLower»Filter"«IF !targets('1.3.5')» class="col-lg-3 control-label"«ENDIF»>{gt text='Filter (expert option)'}:</label>
            «IF !targets('1.3.5')»
                <div class="col-lg-9">
            «ENDIF»
                <input type="text" id="«appName.toFirstLower»Filter" name="filter" size="40" value="{$filterValue|default:''}"«IF !targets('1.3.5')» class="form-control"«ENDIF» />
                «IF targets('1.3.5')»
                    <span class="z-sub z-formnote">
                        ({gt text='Syntax examples'}: <kbd>name:like:foobar</kbd> {gt text='or'} <kbd>status:ne:3</kbd>)
                    </span>
                «ELSE»
                    <span class="help-block">
                        <a class="fa fa-filter" data-toggle="modal" data-target="#filterSyntaxModal">{gt text='Show syntax examples'}</a>
                    </span>
                «ENDIF»
            «IF !targets('1.3.5')»
                </div>
            «ENDIF»
        </div>
        «IF !targets('1.3.5')»

            {include file='include_filterSyntaxDialog.tpl'}
        «ENDIF»

        {pageaddvar name='javascript' value='prototype'}
        <script type="text/javascript">
        /* <![CDATA[ */
            function «prefix()»ToggleCustomTemplate() {
                if ($F('«appName.toFirstLower»Template') == 'custom') {
                    $('customTemplateArea').removeClassName('«IF targets('1.3.5')»z-«ENDIF»hide');
                } else {
                    $('customTemplateArea').addClassName('«IF targets('1.3.5')»z-«ENDIF»hide');
                }
            }

            document.observe('dom:loaded', function() {
                «prefix()»ToggleCustomTemplate();
                $('«appName.toFirstLower»Template').observe('change', function(e) {
                    «prefix()»ToggleCustomTemplate();
                });
            });
        /* ]]> */
        </script>
    '''
}
