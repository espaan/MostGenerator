package org.zikula.modulestudio.generator.application;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import de.guite.modulestudio.metamodel.modulestudio.Application;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.zikula.modulestudio.generator.application.WorkflowPostProcess;
import org.zikula.modulestudio.generator.application.WorkflowPreProcess;
import org.zikula.modulestudio.generator.application.WorkflowSettings;
import org.zikula.modulestudio.generator.cartridges.MostGenerator;
import org.zikula.modulestudio.generator.cartridges.MostGeneratorSetup;
import org.zikula.modulestudio.generator.exceptions.ExceptionBase;
import org.zikula.modulestudio.generator.exceptions.M2TFailedGeneratorResourceNotFound;
import org.zikula.modulestudio.generator.exceptions.M2TUnknownException;
import org.zikula.modulestudio.generator.workflow.components.ModelReader;

/**
 * Main entry point for the workflow.
 */
@SuppressWarnings("all")
public class WorkflowStart {
  private String currentCartridge = "";
  
  public WorkflowSettings settings = new Function0<WorkflowSettings>() {
    public WorkflowSettings apply() {
      WorkflowSettings _workflowSettings = new WorkflowSettings();
      return _workflowSettings;
    }
  }.apply();
  
  public WorkflowPreProcess preProcess = new Function0<WorkflowPreProcess>() {
    public WorkflowPreProcess apply() {
      WorkflowPreProcess _workflowPreProcess = new WorkflowPreProcess();
      return _workflowPreProcess;
    }
  }.apply();
  
  private Resource model = null;
  
  /**
   * Validate the model
   */
  public boolean validate() {
    final IProgressMonitor progressMonitor = this.settings.getProgressMonitor();
    String _appName = this.settings.getAppName();
    String _plus = ("Validating \"" + _appName);
    String _plus_1 = (_plus + " ");
    String _appVersion = this.settings.getAppVersion();
    String _plus_2 = (_plus_1 + _appVersion);
    String _plus_3 = (_plus_2 + "\" ...");
    int _minus = (-1);
    progressMonitor.beginTask(_plus_3, _minus);
    Resource _model = this.getModel();
    EList<EObject> _contents = _model.getContents();
    EObject _get = _contents.get(0);
    Diagnostic diag = Diagnostician.INSTANCE.validate(_get);
    int _severity = diag.getSeverity();
    final int _switchValue = _severity;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,Diagnostic.ERROR)) {
        _matched=true;
        String _string = diag.toString();
        String _plus_4 = ("Errors: " + _string);
        progressMonitor.subTask(_plus_4);
        progressMonitor.done();
        return false;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,Diagnostic.WARNING)) {
        _matched=true;
        String _string_1 = diag.toString();
        String _plus_5 = ("Warnings: " + _string_1);
        progressMonitor.subTask(_plus_5);
        progressMonitor.done();
        return true;
      }
    }
    {
      progressMonitor.subTask("Valid");
      progressMonitor.done();
      return true;
    }
  }
  
  /**
   * Executes the workflow; preProcess.run() has already been called.
   */
  public void run() throws ExceptionBase {
    this.performM2T();
    WorkflowPostProcess _workflowPostProcess = new WorkflowPostProcess(this.settings);
    _workflowPostProcess.run();
  }
  
  /**
   * Workflow facade executing the actual model-to-text workflows.
   */
  private boolean performM2T() throws ExceptionBase {
    boolean _xblockexpression = false;
    {
      boolean _and = false;
      ArrayList<Object> _selectedCartridges = this.settings.getSelectedCartridges();
      int _size = _selectedCartridges.size();
      boolean _equals = (_size == 1);
      if (!_equals) {
        _and = false;
      } else {
        ArrayList<Object> _selectedCartridges_1 = this.settings.getSelectedCartridges();
        Object _head = IterableExtensions.<Object>head(_selectedCartridges_1);
        boolean _equals_1 = Objects.equal(_head, "reporting");
        _and = (_equals && _equals_1);
      }
      if (_and) {
        return false;
      }
      boolean success = false;
      try {
        final IProgressMonitor progressMonitor = this.settings.getProgressMonitor();
        String _appName = this.settings.getAppName();
        String _plus = ("Generating \"" + _appName);
        String _plus_1 = (_plus + " ");
        String _appVersion = this.settings.getAppVersion();
        String _plus_2 = (_plus_1 + _appVersion);
        String _plus_3 = (_plus_2 + "\" ...");
        int _minus = (-1);
        progressMonitor.beginTask(_plus_3, _minus);
        ArrayList<Object> _selectedCartridges_2 = this.settings.getSelectedCartridges();
        for (final Object singleCartridge : _selectedCartridges_2) {
          {
            String _string = singleCartridge.toString();
            this.currentCartridge = _string;
            boolean _equals_2 = this.currentCartridge.equals("reporting");
            boolean _not = (!_equals_2);
            if (_not) {
              MostGenerator _mostGenerator = new MostGenerator();
              final MostGenerator generator = _mostGenerator;
              generator.setCartridge(this.currentCartridge);
              IProgressMonitor _progressMonitor = this.settings.getProgressMonitor();
              generator.setMonitor(_progressMonitor);
              final JavaIoFileSystemAccess fileSystemAccess = this.getConfiguredFileSystemAccess();
              Resource _model = this.getModel();
              generator.doGenerate(_model, fileSystemAccess);
            }
          }
        }
        success = true;
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          M2TFailedGeneratorResourceNotFound _m2TFailedGeneratorResourceNotFound = new M2TFailedGeneratorResourceNotFound();
          throw _m2TFailedGeneratorResourceNotFound;
        } else if (_t instanceof Exception) {
          final Exception e_1 = (Exception)_t;
          e_1.printStackTrace();
          M2TUnknownException _m2TUnknownException = new M2TUnknownException();
          throw _m2TUnknownException;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      } finally {
      }
      _xblockexpression = (success);
    }
    return _xblockexpression;
  }
  
  protected JavaIoFileSystemAccess getConfiguredFileSystemAccess() {
    JavaIoFileSystemAccess _xblockexpression = null;
    {
      MostGeneratorSetup _mostGeneratorSetup = new MostGeneratorSetup();
      final MostGeneratorSetup setup = _mostGeneratorSetup;
      final Injector injector = setup.createInjectorAndDoEMFRegistration();
      final JavaIoFileSystemAccess configuredFileSystemAccess = injector.<JavaIoFileSystemAccess>getInstance(JavaIoFileSystemAccess.class);
      String _outputPath = this.settings.getOutputPath();
      String _plus = (_outputPath + "/");
      String _plus_1 = (_plus + this.currentCartridge);
      String _plus_2 = (_plus_1 + "/");
      String _appName = this.settings.getAppName();
      String _plus_3 = (_plus_2 + _appName);
      String _plus_4 = (_plus_3 + "/");
      configuredFileSystemAccess.setOutputPath(
        "DEFAULT_OUTPUT", _plus_4);
      _xblockexpression = (configuredFileSystemAccess);
    }
    return _xblockexpression;
  }
  
  private Resource getModel() {
    Resource _xblockexpression = null;
    {
      boolean _tripleEquals = (this.model == null);
      if (_tripleEquals) {
        ModelReader _modelReader = new ModelReader();
        final ModelReader reader = _modelReader;
        String _modelPath = this.settings.getModelPath();
        reader.setUri(_modelPath);
        Resource _invoke = reader.invoke();
        this.model = _invoke;
      }
      _xblockexpression = (this.model);
    }
    return _xblockexpression;
  }
  
  public void readSettingsFromModel() {
    final Resource model = this.getModel();
    EList<EObject> _contents = model.getContents();
    EObject _get = _contents.get(0);
    final Application app = ((Application) _get);
    String _name = app.getName();
    InputOutput.<String>println(_name);
    String _name_1 = app.getName();
    this.settings.setAppName(_name_1);
    String _version = app.getVersion();
    this.settings.setAppVersion(_version);
    return;
  }
}