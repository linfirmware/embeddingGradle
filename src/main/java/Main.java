import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.gradle.tooling.*;
import org.gradle.tooling.model.*;
import org.gradle.tooling.model.idea.*;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        // Configure the connector and create the connection
        GradleConnector connector = GradleConnector.newConnector();

        if (args.length > 0) {
            connector.useInstallation(new File(args[0]));
            if (args.length > 1) {
                connector.useGradleUserHomeDir(new File(args[1]));
            }
        }

        connector.forProjectDirectory(new File("."));

        ProjectConnection connection = connector.connect();
        try {
            IdeaProject project = connection.getModel(IdeaProject.class);
            System.out.println("***");
            System.out.println("***");
            System.out.println("Project details: ");
            System.out.println(project);

            System.out.println("***");
            System.out.println("Project modules: ");
            for(IdeaModule module: project.getModules()) {
                System.out.println("  " + module);
                System.out.println("  module details:");

                System.out.println("    tasks from associated gradle project:");
                for (GradleTask task: module.getGradleProject().getTasks()) {
                    System.out.println("      " + task.getName());
                }

                for (IdeaContentRoot root: module.getContentRoots()) {
                    System.out.println("    Content root: " + root.getRootDirectory());
                    System.out.println("    source dirs:");
                    for (IdeaSourceDirectory dir: root.getSourceDirectories()) {
                        System.out.println("      " + dir);
                    }

                    System.out.println("    test dirs:");
                    for (IdeaSourceDirectory dir: root.getTestDirectories()) {
                        System.out.println("      " + dir);
                    }

                    System.out.println("    exclude dirs:");
                    for (File dir: root.getExcludeDirectories()) {
                        System.out.println("      " + dir);
                    }
                }

                System.out.println("    dependencies:");
                for (IdeaDependency dependency: module.getDependencies()) {
                    System.out.println("      * " + dependency);
                }
            }
        } finally {
            // Clean up
            connection.close();
        }

        launch(args);
    }
}
