package org.zikula.modulestudio.generator.cartridges.zclassic.view.extensions;

import com.google.inject.Inject;
import de.guite.modulestudio.metamodel.modulestudio.Application;
import de.guite.modulestudio.metamodel.modulestudio.Controller;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.zikula.modulestudio.generator.extensions.ControllerExtensions;
import org.zikula.modulestudio.generator.extensions.NamingExtensions;
import org.zikula.modulestudio.generator.extensions.Utils;

@SuppressWarnings("all")
public class MetaData {
  @Inject
  @Extension
  private ControllerExtensions _controllerExtensions = new Function0<ControllerExtensions>() {
    public ControllerExtensions apply() {
      ControllerExtensions _controllerExtensions = new ControllerExtensions();
      return _controllerExtensions;
    }
  }.apply();
  
  @Inject
  @Extension
  private NamingExtensions _namingExtensions = new Function0<NamingExtensions>() {
    public NamingExtensions apply() {
      NamingExtensions _namingExtensions = new NamingExtensions();
      return _namingExtensions;
    }
  }.apply();
  
  @Inject
  @Extension
  private Utils _utils = new Function0<Utils>() {
    public Utils apply() {
      Utils _utils = new Utils();
      return _utils;
    }
  }.apply();
  
  public void generate(final Application it, final Controller controller, final IFileSystemAccess fsa) {
    String _viewPath = this._namingExtensions.getViewPath(it);
    String _xifexpression = null;
    boolean _targets = this._utils.targets(it, "1.3.5");
    if (_targets) {
      String _formattedName = this._controllerExtensions.formattedName(controller);
      _xifexpression = _formattedName;
    } else {
      String _formattedName_1 = this._controllerExtensions.formattedName(controller);
      String _firstUpper = StringExtensions.toFirstUpper(_formattedName_1);
      _xifexpression = _firstUpper;
    }
    String _plus = (_viewPath + _xifexpression);
    final String templatePath = (_plus + "/");
    boolean _or = false;
    boolean _hasActions = this._controllerExtensions.hasActions(controller, "view");
    if (_hasActions) {
      _or = true;
    } else {
      boolean _hasActions_1 = this._controllerExtensions.hasActions(controller, "display");
      _or = (_hasActions || _hasActions_1);
    }
    if (_or) {
      String _plus_1 = (templatePath + "include_metadata_display.tpl");
      CharSequence _metaDataViewImpl = this.metaDataViewImpl(it, controller);
      fsa.generateFile(_plus_1, _metaDataViewImpl);
    }
    boolean _hasActions_2 = this._controllerExtensions.hasActions(controller, "edit");
    if (_hasActions_2) {
      String _plus_2 = (templatePath + "include_metadata_edit.tpl");
      CharSequence _metaDataEditImpl = this.metaDataEditImpl(it, controller);
      fsa.generateFile(_plus_2, _metaDataEditImpl);
    }
  }
  
  private CharSequence metaDataViewImpl(final Application it, final Controller controller) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{* purpose of this template: reusable display of meta data fields *}");
    _builder.newLine();
    _builder.append("{if isset($obj.metadata)}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if isset($panel) && $panel eq true}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<h3 class=\"metadata z-panel-header z-panel-indicator z-pointer\">{gt text=\'Metadata\'}</h3>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<div class=\"metadata z-panel-content\" style=\"display: none\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{else}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<h3 class=\"metadata\">{gt text=\'Metadata\'}</h3>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<dl class=\"propertylist\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.title ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Title\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.title|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.author ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Author\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.author|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.subject ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Subject\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.subject|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.keywords ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Keywords\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.keywords|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.description ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Description\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.description|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.publisher ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Publisher\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.publisher|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.contributor ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Contributor\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.contributor|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.startdate ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Start date\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.startdate|dateformat}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.enddate ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'End date\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.enddate|dateformat}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.type ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Type\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.type|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.format ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Format\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.format|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.uri ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Uri\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.uri|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.source ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Source\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.source|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.language ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Language\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.language|getlanguagename|safehtml}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.relation ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Relation\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.relation|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.coverage ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Coverage\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.coverage|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.comment ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Comment\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.comment|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $obj.metadata.extra ne \'\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dt>{gt text=\'Extra\'}</dt>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dd>{$obj.metadata.extra|default:\'-\'|safetext}</dd>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</dl>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if isset($panel) && $panel eq true}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("{/if}");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence metaDataEditImpl(final Application it, final Controller controller) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{* purpose of this template: reusable editing of meta data fields *}");
    _builder.newLine();
    _builder.append("{if isset($panel) && $panel eq true}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<h3 class=\"metadata z-panel-header z-panel-indicator z-pointer\">{gt text=\'Metadata\'}</h3>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<fieldset class=\"metadata z-panel-content\" style=\"display: none\">");
    _builder.newLine();
    _builder.append("{else}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<fieldset class=\"metadata\">");
    _builder.newLine();
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<legend>{gt text=\'Metadata\'}</legend>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataTitle\' __text=\'Title\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataTitle\' dataField=\'title\' maxLength=80}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataAuthor\' __text=\'Author\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataAuthor\' dataField=\'author\' maxLength=80}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataSubject\' __text=\'Subject\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataSubject\' dataField=\'subject\' maxLength=255}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataKeywords\' __text=\'Keywords\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataKeywords\' dataField=\'keywords\' maxLength=128}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataDescription\' __text=\'Description\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataDescription\' dataField=\'description\' maxLength=255}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataPublisher\' __text=\'Publisher\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataPublisher\' dataField=\'publisher\' maxLength=128}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataContributor\' __text=\'Contributor\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataContributor\' dataField=\'contributor\' maxLength=80}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataStartdate\' __text=\'Start date\'}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $mode ne \'create\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formdateinput group=\'meta\' id=\'metadataStartdate\' dataField=\'startdate\' mandatory=false includeTime=true}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{else}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formdateinput group=\'meta\' id=\'metadataStartdate\' dataField=\'startdate\' mandatory=false includeTime=true defaultValue=\'now\'}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataEnddate\' __text=\'End date\'}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{if $mode ne \'create\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formdateinput group=\'meta\' id=\'metadataEnddate\' dataField=\'enddate\' mandatory=false includeTime=true}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{else}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formdateinput group=\'meta\' id=\'metadataEnddate\' dataField=\'enddate\' mandatory=false includeTime=true defaultValue=\'now\'}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{/if}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataType\' __text=\'Type\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataType\' dataField=\'type\' maxLength=128}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataFormat\' __text=\'Format\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataFormat\' dataField=\'format\' maxLength=128}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataUri\' __text=\'Uri\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataUri\' dataField=\'uri\' maxLength=255}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataSource\' __text=\'Source\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataSource\' dataField=\'source\' maxLength=128}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataLanguage\' __text=\'Language\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlanguageselector group=\'meta\' id=\'metadataLanguage\' mandatory=false __title=\'Choose a language\' dataField=\'language\'}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataRelation\' __text=\'Relation\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataRelation\' dataField=\'relation\' maxLength=255}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataCoverage\' __text=\'Coverage\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataCoverage\' dataField=\'coverage\' maxLength=64}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataComment\' __text=\'Comment\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataComment\' dataField=\'comment\' maxLength=255}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div class=\"z-formrow\">");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formlabel for=\'metadataExtra\' __text=\'Extra\'}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("{formtextinput group=\'meta\' id=\'metadataExtra\' dataField=\'extra\' maxLength=255}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.append("</fieldset>");
    _builder.newLine();
    return _builder;
  }
}
