package org.zikula.modulestudio.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.zikula.modulestudio.generator.application.Activator;

import de.guite.modulestudio.metamodel.modulestudio.Application;

public class WorkflowSettings {

    ArrayList aviableCartridges = new ArrayList();
    Object[] selectedCartridges;
    String outputPath = null;
    File outputDir = null;
    String modelPath = null;
    Diagram diagram = null;
    Application app = null;
    PreferencesHint diagramPreferencesHint = null;
    IProgressMonitor progressMonitor = null;
    File reportDir = null;
    ArrayList aviableReports = new ArrayList();
    Object[] selectedReports = null;
    IWorkbenchWindow workbenchWindow = null;
    IWorkbench workbench = null;

    public WorkflowSettings() {
        this.aviableCartridges.add("zclassic");
        this.aviableCartridges.add("zoo");
        this.aviableCartridges.add("reporting");

        final java.net.URL[] resources = FileLocator.findEntries(Platform
                .getBundle(Activator.PLUGIN_ID), new Path(
                "/src/templates/reporting/reports"));
        try {
            reportDir = new File(FileLocator.toFileURL(resources[0]).toURI());

            for (final String file : this.reportDir.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (name.contains(".rptdesign")) {
                        return true;
                    }
                    return false;
                }
            })) {
                this.aviableReports.add(file.replace(".rptdesign", ""));
            }

        } catch (final URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public IWorkbenchWindow getWorkbenchWindow() {
        return this.workbenchWindow;
    }

    public void setWorkbenchWindow(IWorkbenchWindow window) {
        this.workbenchWindow = window;
    }

    public IWorkbench getWorkbench() {
        return this.workbench;
    }

    public void setWorkbench(IWorkbench workbench) {
        this.workbench = workbench;
    }

    public ArrayList getAviableCartridges() {
        return this.aviableCartridges;
    }

    public ArrayList getAviableReports() {
        return this.aviableReports;
    }

    public void setApp(Application app) {
        this.app = app;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    public void setDiagramPreferencesHint(PreferencesHint hint) {
        this.diagramPreferencesHint = hint;
    }

    public void setProgressMonitor(IProgressMonitor monitor) {
        this.progressMonitor = monitor;
    }

    public void setOutputPath(String path) {
        this.outputPath = path;
        this.outputDir = new File(outputPath);
    }

    public void setModelPath(String path) {
        this.modelPath = path;
    }

    public void setSelectedCartridges(Object[] cartridges) {
        this.selectedCartridges = cartridges;
    }

    public void setSelectedReports(Object[] reports) {
        this.selectedReports = reports;
    }

}
