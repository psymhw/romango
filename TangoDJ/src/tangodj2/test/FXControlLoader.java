package tangodj2.test;

/**
 * Copyright 2012 - 2013 Andy Till
 * 
 * This file is part of EstiMate.
 * 
 * EstiMate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EstiMate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EstiMate.  If not, see <http://www.gnu.org/licenses/>.
 */



import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import projmon.guice.FXMLLoadingScope;
import projmon.skills.SkillFormController;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FXControlLoader {
    
    private static class ControlContainer {
        public  Parent control;
        public  Object controller;
    }
    
    public static class FXControlLoad {
        public final Parent control;
        public final Object controller;
        public Object entity;
        
        public FXControlLoad(Parent control, Object controller) {
            this.control = control;
            this.controller = controller;
        }
        
        public FXControlLoad withEntity(Object anEntity) {
            entity = anEntity;
            return this;
        }
    }
        
        private final Callback<Class<?>, Object> controllerFactory;

    private final FXMLLoadingScope loadingScope;
        
        private final ControlContainer personForm;

    private final ControlContainer projectForm;

    private final ControlContainer taskForm;

    private final ControlContainer skillForm;

    private final ControlContainer mainScreenForm;
        
    @Inject
        public FXControlLoader(GuiceControllerFactory aControllerFactory, FXMLLoadingScope aLoadingScope) {
                controllerFactory = aControllerFactory;
        this.loadingScope = aLoadingScope;
        
        projectForm = new ControlContainer();
        personForm = new ControlContainer();
        taskForm = new ControlContainer();
        skillForm = new ControlContainer();
        mainScreenForm = new ControlContainer();
        }

        public Parent loadMainScreen() {
                return loadControl("/projmon/gui/project screen.fxml", mainScreenForm).control;
        }

        public FXControlLoad loadProjectForm(Project newValue) {
            FXControlLoad fx;
        
        fx = loadControl("/projmon/gui/projectForm.fxml", projectForm);
        fx.entity = newValue;
        
        return fx;
        }

        public FXControlLoad loadPersonForm(Person newValue) {
        FXControlLoad fx;
        
        fx = loadControl("/projmon/gui/personForm.fxml", personForm);
        fx.entity = newValue;
        
        return fx;
        }

    public FXControlLoad loadTaskForm(WorkTask task) {
        return loadControl("/projmon/gui/taskForm.fxml", taskForm).withEntity(task);
    }
    
    public FXControlLoad loadEmptySkillForm(EventHandler<ActionEvent> cancelActionHandler) {
        
        if(skillForm.controller != null) {
            ((SkillFormController)skillForm.controller).reset();
        }
        
        FXControlLoad loadControl = loadControl("/projmon/skills/skillForm.fxml", skillForm);
        
        ((SkillFormController)loadControl.controller).setCancelActionHandler(cancelActionHandler);
        
        return loadControl;
    }

    public FXControlLoad loadSkillProficiencyCell() {
        return loadControl("/projmon/skills/skillProficiencyCell.fxml");
    }
    
    public FXControlLoad loadProjectCellNode() {
        return loadControl("/projmon/gui/projectCell.fxml");
    }

    private FXControlLoad loadControl(String controlClasspath, ControlContainer controlContainer) {

        loadingScope.enter();
        
        try {
            Parent parent = null;
            Object controller = null;
            
            if(controlContainer == null || controlContainer.control == null) {

                URL resource = getClass().getResource(controlClasspath);

                assert resource != null : "Control at URL '" + controlClasspath + "' was not found.";
                
                FXMLLoader loader;
                
                loader = new FXMLLoader();         
                loader.setControllerFactory(controllerFactory);
                loader.setLocation(resource);
                
                parent = (Parent) loader.load(resource.openStream());
                controller = loader.getController();
                
                if(controlContainer != null) {
                    controlContainer.control = parent;
                    controlContainer.controller = loader.getController();
                }
            }
            else {
                parent = controlContainer.control;
                controller = controlContainer.controller;
            }
            
            return new FXControlLoad(parent, controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            loadingScope.exit();
        }
    }

    private FXControlLoad loadControl(String controlClasspath) {
        return loadControl(controlClasspath, null);
    }

    public FXControlLoad loadWorkTree() {
        return loadControl("/projmon/gui/workTree.fxml");
    }

    public FXControlLoad loadRootView() {
        return loadControl("/projmon/gui/rootView.fxml");
    }

    public FXControlLoad loadTemplateScreen() {
        return loadControl("/projmon/gui/taskTemplates.fxml");
    }

    public FXControlLoad loadLogin() {
        return loadControl("/projmon/gui/login.fxml");
    }

    public FXControlLoad loadTaskCompleteForm() {
        return loadControl("/projmon/gui/taskCompleteDialog.fxml");
    }

    public FXControlLoad loadPersonCard() {
        return loadControl("/projmon/gui/personCard.fxml");
    }
}