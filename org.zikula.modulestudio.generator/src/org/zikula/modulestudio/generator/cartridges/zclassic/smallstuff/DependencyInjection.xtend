package org.zikula.modulestudio.generator.cartridges.zclassic.smallstuff

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import org.eclipse.xtext.generator.IFileSystemAccess
import org.zikula.modulestudio.generator.extensions.FormattingExtensions
import org.zikula.modulestudio.generator.extensions.GeneratorSettingsExtensions
import org.zikula.modulestudio.generator.extensions.NamingExtensions
import org.zikula.modulestudio.generator.extensions.Utils

class DependencyInjection {
    @Inject extension FormattingExtensions = new FormattingExtensions
    @Inject extension GeneratorSettingsExtensions = new GeneratorSettingsExtensions
    @Inject extension NamingExtensions = new NamingExtensions
    @Inject extension Utils = new Utils

    FileHelper fh = new FileHelper

    def generate(Application it, IFileSystemAccess fsa) {
        if (targets('1.3.5')) {
            return
        }
        val extensionFileName = vendor.formatForCodeCapital + name.formatForCodeCapital + 'Extension.php'
        if (!shouldBeSkipped(getAppSourceLibPath + 'DependencyInjection/Base/' + extensionFileName)) {
            fsa.generateFile(getAppSourceLibPath + 'DependencyInjection/Base/' + extensionFileName, extensionBaseFile)
        }
        if (!generateOnlyBaseClasses && !shouldBeSkipped(getAppSourceLibPath + 'DependencyInjection/' + extensionFileName)) {
            fsa.generateFile(getAppSourceLibPath + 'DependencyInjection/' + extensionFileName, extensionFile)
        }
    }

    def private extensionBaseFile(Application it) '''
        «fh.phpFileHeaderVersionClass(it)»
        «extensionBaseImpl»
    '''

    def private extensionFile(Application it) '''
        «fh.phpFileHeaderVersionClass(it)»
        «extensionImpl»
    '''

    def private extensionBaseImpl(Application it) '''
        namespace «appNamespace»\DependencyInjection\Base;

        use Symfony\Component\Config\FileLocator;
        use Symfony\Component\DependencyInjection\ContainerBuilder;
        use Symfony\Component\DependencyInjection\Loader\XmlFileLoader;
        use Symfony\Component\HttpKernel\DependencyInjection\Extension;

        /**
         * Base class for service definition loader using the DependencyInjection extension.
         */
        class «vendor.formatForCodeCapital»«name.formatForCodeCapital»Extension extends Extension
        {
            /**
             * Loads service definition file containing persistent event handlers.
             * Responds to the app.config configuration parameter.
             *
             * @param array            $configs
             * @param ContainerBuilder $container
             */
            public function load(array $configs, ContainerBuilder $container)
            {
                $loader = new XmlFileLoader($container, new FileLocator(__DIR__ . '/../Resources/config'));
        
                $loader->load('services.xml');
            }
        }
    '''

    def private extensionImpl(Application it) '''
        namespace «appNamespace»\DependencyInjection;

        use «appNamespace»\DependencyInjection\Base\«vendor.formatForCodeCapital»«name.formatForCodeCapital»Extension as Base«vendor.formatForCodeCapital»«name.formatForCodeCapital»Extension;

        /**
         * Implementation class for service definition loader using the DependencyInjection extension.
         */
        class «vendor.formatForCodeCapital»«name.formatForCodeCapital»Extension extends Base«vendor.formatForCodeCapital»«name.formatForCodeCapital»Extension
        {
            // custom enhancements can go here
        }
    '''
}