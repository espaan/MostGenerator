package org.zikula.modulestudio.generator.cartridges.zclassic.view.pages

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import de.guite.modulestudio.metamodel.modulestudio.Controller
import de.guite.modulestudio.metamodel.modulestudio.Entity
import org.eclipse.xtext.generator.IFileSystemAccess
import org.zikula.modulestudio.generator.extensions.ControllerExtensions
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.Utils

class Index {
    @Inject extension ControllerExtensions = new ControllerExtensions()
    @Inject extension FormattingExtensions = new FormattingExtensions()
    @Inject extension NamingExtensions = new NamingExtensions()
    @Inject extension Utils = new Utils()

    Application app

    def generate(Entity it, Controller controller, IFileSystemAccess fsa) {
        app = container.application
        val pageName = (if (app.targets('1.3.5')) 'main' else 'index')
        println('Generating ' + controller.formattedName + ' ' + pageName + ' templates for entity "' + name.formatForDisplay + '"')
        val app = container.application
        val templatePath = app.getViewPath + (if (app.targets('1.3.5')) controller.formattedName else controller.formattedName.toFirstUpper) + '/'
        fsa.generateFile(templatePath + pageName + '.tpl', indexView(pageName, controller))
    }

    def private indexView(Entity it, String pageName, Controller controller) '''
        {* purpose of this template: «nameMultiple.formatForDisplay» «pageName» view in «controller.formattedName» area *}
        «IF controller.hasActions('view')»
        {modfunc modname='«app.appName»' type='«controller.formattedName»' func='view'}
        «ELSE»
            {include file='«IF app.targets('1.3.5')»«controller.formattedName»«ELSE»«controller.formattedName.toFirstUpper»«ENDIF»/header.tpl'}
            <p>{gt text='Welcome to the «controller.formattedName» section of the «app.appName.formatForDisplayCapital» application.'}</p>
            {include file='«IF app.targets('1.3.5')»«controller.formattedName»«ELSE»«controller.formattedName.toFirstUpper»«ENDIF»/footer.tpl'}
        «ENDIF»
    '''
}