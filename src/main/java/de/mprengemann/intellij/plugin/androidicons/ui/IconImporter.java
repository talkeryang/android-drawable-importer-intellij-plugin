/*
 * Copyright 2015 Marc Prengemann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * 			http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package de.mprengemann.intellij.plugin.androidicons.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.SystemInfo;
import de.mprengemann.intellij.plugin.androidicons.IconApplication;
import de.mprengemann.intellij.plugin.androidicons.controllers.iconimporter.IIconsImporterController;
import de.mprengemann.intellij.plugin.androidicons.controllers.iconimporter.IconsImporterController;
import de.mprengemann.intellij.plugin.androidicons.controllers.iconimporter.IconsImporterObserver;
import de.mprengemann.intellij.plugin.androidicons.controllers.icons.androidicons.IAndroidIconsController;
import de.mprengemann.intellij.plugin.androidicons.controllers.icons.materialicons.IMaterialIconsController;
import de.mprengemann.intellij.plugin.androidicons.controllers.settings.ISettingsController;
import de.mprengemann.intellij.plugin.androidicons.model.IconPack;
import de.mprengemann.intellij.plugin.androidicons.model.ImageAsset;
import de.mprengemann.intellij.plugin.androidicons.util.ImageUtils;
import de.mprengemann.intellij.plugin.androidicons.widgets.ResourceBrowser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.List;

public class IconImporter extends DialogWrapper implements IconsImporterObserver {

    private final Project project;
    private final IAndroidIconsController androidIconsController;
    private final IMaterialIconsController materialIconsController;
    private final ISettingsController settingsController;
    private final IIconsImporterController iconImporterController;

    private JLabel imageContainer;
    private JComboBox assetSpinner;
    private JComboBox colorSpinner;
    private JComboBox categorySpinner;
    private JComboBox sizeSpinner;

    private JPanel uiContainer;
    private ResourceBrowser resRoot;
    private JTextField resExportName;
    private JCheckBox LDPICheckBox;
    private JCheckBox MDPICheckBox;
    private JCheckBox HDPICheckBox;
    private JCheckBox XHDPICheckBox;
    private JCheckBox XXHDPICheckBox;
    private JCheckBox XXXHDPICheckBox;
    private JComboBox iconPackSpinner;
    private JComboBox searchField;
    private final ActionListener iconPackActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final JComboBox source = (JComboBox) actionEvent.getSource();
            final IconPack selectedItem = (IconPack) source.getSelectedItem();
            iconImporterController.setSelectedIconPack(selectedItem.getId());
        }
    };
    private final ActionListener categoryActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final JComboBox source = (JComboBox) actionEvent.getSource();
            final String selectedItem = (String) source.getSelectedItem();
            iconImporterController.setSelectedCategory(selectedItem);
        }
    };
    private final ActionListener assetActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final JComboBox source = (JComboBox) actionEvent.getSource();
            final ImageAsset selectedItem = (ImageAsset) source.getSelectedItem();
            iconImporterController.setSelectedAsset(selectedItem);
        }
    };
    private final ActionListener sizeActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final JComboBox source = (JComboBox) actionEvent.getSource();
            final String selectedItem = (String) source.getSelectedItem();
            iconImporterController.setSelectedSize(selectedItem);
        }
    };
    private final ActionListener colorActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final JComboBox source = (JComboBox) actionEvent.getSource();
            final String selectedItem = (String) source.getSelectedItem();
            iconImporterController.setSelectedColor(selectedItem);
        }
    };

    public IconImporter(Project project, Module module) {
        super(project, true);

        final IconApplication container = ApplicationManager.getApplication().getComponent(IconApplication.class);
        androidIconsController = container.getControllerFactory().getAndroidIconsController();
        materialIconsController = container.getControllerFactory().getMaterialIconsController();
        settingsController = container.getControllerFactory().getSettingsController();

        iconImporterController = new IconsImporterController(androidIconsController, materialIconsController);
        this.project = project;
//
//        resRoot.setSelectionListener(new Consumer<File>() {
//            @Override
//            public void consume(File file) {
//                String path;
//                if (file == null) {
//                    path = "";
//                } else {
//                    path = file.getPath();
//                }
//                iconImporterController.setExportRoot(path);
//            }
//        });
//        resRoot.init(project, module, container.getControllerFactory().getSettingsController());
//
        setTitle("Icon Importer");
        setResizable(false);
        getHelpAction().setEnabled(true);

        AssetSpinnerRenderer renderer = new AssetSpinnerRenderer();
        //noinspection GtkPreferredJComboBoxRenderer
        assetSpinner.setRenderer(renderer);
//        resExportName.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent keyEvent) {}
//
//            @Override
//            public void keyPressed(KeyEvent keyEvent) {}
//
//            @Override
//            public void keyReleased(KeyEvent keyEvent) {
//                iconImporterController.setExportName(((JTextField) keyEvent.getSource()).getText());
//            }
//        });
//        imageContainer.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                super.componentResized(e);
//                updateImage();
//            }
//        });
//
//        initCheckBox(Resolution.LDPI, LDPICheckBox);
//        initCheckBox(Resolution.MDPI, MDPICheckBox);
//        initCheckBox(Resolution.HDPI, HDPICheckBox);
//        initCheckBox(Resolution.XHDPI, XHDPICheckBox);
//        initCheckBox(Resolution.XXHDPI, XXHDPICheckBox);
//        initCheckBox(Resolution.XXXHDPI, XXXHDPICheckBox);
//        initSearch();

        iconImporterController.addObserver(this);
        init();
    }

//    private void initSearch() {
//        final EventList<ImageAsset> assets = GlazedLists.eventList(null);
//        assets.addAll(androidIconsController.getAssets());
//        assets.addAll(materialIconsController.getAssets());
//        for (ImageAsset asset : assets) {
//            searchField.addItem(asset);
//        }
//
//        TextFilterator<ImageAsset> textFilterator =
//            GlazedLists.textFilterator(ImageAsset.class, "name");
//        AutoCompleteSupport support =
//            AutoCompleteSupport.install(searchField, assets, textFilterator, new AssetFormat());
//
//        support.setStrict(true);
//        support.setHidesPopupOnFocusLost(true);
//        support.setBeepOnStrictViolation(true);
//        support.setCorrectsCase(true);
//
//        //noinspection GtkPreferredJComboBoxRenderer
//        searchField.setRenderer(new AssetSpinnerRenderer());
//        searchField.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent event) {
//                final ImageAsset selectedItem = (ImageAsset) searchField.getSelectedItem();
//                if (selectedItem == null) {
//                    return;
//                }
//                iconImporterController.setSelectedIconPack(selectedItem.getId());
//                if (selectedItem.getId() == IconPack.MATERIAL_ICONS) {
//                    iconImporterController.setSelectedCategory(selectedItem.getCategory());
//                }
//                iconImporterController.setSelectedAsset(selectedItem);
//            }
//        });
//    }
//
//    private void initCheckBox(final Resolution resolution, final JCheckBox checkBox) {
//        checkBox.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent itemEvent) {
//                iconImporterController.setExportResolution(resolution, checkBox.isSelected());
//            }
//        });
//        checkBox.setSelected(true);
//    }

    @NotNull
    @Override
    public Action[] createActions() {
        return SystemInfo.isMac ? new Action[] {this.getHelpAction(), this.getCancelAction(), this.getOKAction()}
                                : new Action[] {this.getOKAction(), this.getCancelAction(), this.getHelpAction()};
    }

    private void prepareSpinner(JComboBox comboBox, ActionListener listener) {
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(null);
        comboBox.removeAllItems();
    }

    private void initSpinner(JComboBox comboBox, Object selectedItem, ActionListener listener) {
        comboBox.setSelectedItem(selectedItem);
        comboBox.addActionListener(listener);
    }

    private void updatePacks() {
        prepareSpinner(iconPackSpinner, iconPackActionListener);
        iconPackSpinner.addItem(androidIconsController.getIconPack());
        iconPackSpinner.addItem(materialIconsController.getIconPack());
        initSpinner(iconPackSpinner, iconImporterController.getSelectedIconPack().getIconPack(), iconPackActionListener);
    }

    private void updateCategories() {
        prepareSpinner(categorySpinner, categoryActionListener);
        for (String category : iconImporterController.getCategories()) {
            categorySpinner.addItem(category);
        }
        categorySpinner.setEnabled(categorySpinner.getItemCount() > 1);
        initSpinner(categorySpinner, iconImporterController.getSelectedCategory(), categoryActionListener);
    }

    private void updateAssets() {
        prepareSpinner(assetSpinner, assetActionListener);
        List<ImageAsset> assets = iconImporterController.getAssets();
        for (ImageAsset asset : assets) {
            assetSpinner.addItem(asset);
        }
        initSpinner(assetSpinner, iconImporterController.getSelectedAsset(), assetActionListener);
    }

    private void updateSizes() {
        prepareSpinner(sizeSpinner, sizeActionListener);
        List<String> sizes = iconImporterController.getSizes();
        for (String size : sizes) {
            sizeSpinner.addItem(size);
        }
        initSpinner(sizeSpinner, iconImporterController.getSelectedSize(), sizeActionListener);
    }

    private void updateColors() {
        prepareSpinner(colorSpinner, colorActionListener);
        List<String> colors = iconImporterController.getColors();
        for (String color : colors) {
            colorSpinner.addItem(color);
        }
        initSpinner(colorSpinner, iconImporterController.getSelectedColor(), colorActionListener);
    }

    private void updateImage() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (imageContainer == null) {
                    return;
                }
                ImageUtils.updateImage(imageContainer, iconImporterController.getSelectedImageFile());
            }
        });
    }

    @Override
    protected void doHelpAction() {
        materialIconsController.openHelp();
    }

    @Override
    protected void doOKAction() {
//        importIcons();
        super.doOKAction();
//        settingsController.saveResRootForProject(project,
//                                                 "file://" + iconImporterController.getExportRoot());
    }
//
//    private void importIcons() {
//        RefactoringTask task = iconImporterController.getTask(project);
//        ProgressManager.getInstance().run(task);
//    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
//        if (StringUtils.isEmpty(iconImporterController.getExportRoot())) {
//            return new ValidationInfo("Please select the resources root.", resRoot);
//        }
//
//        if (StringUtils.isEmpty(resExportName.getText().trim())) {
//            return new ValidationInfo("Please select a name for the drawable.", resExportName);
//        } else if (!resExportName.getText().matches("[a-z0-9_.]*")) {
//            return new ValidationInfo(
//                "Please select a valid name for the drawable. There are just \"[a-z0-9_.]\" allowed.",
//                resExportName);
//        }

        return null;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return uiContainer;
    }

    @Override
    public void updated() {
        updatePacks();
        updateCategories();
        updateAssets();
        updateSizes();
        updateColors();
        updateImage();
//        resExportName.setText(iconImporterController.getExportName());
//        resRoot.setText(iconImporterController.getExportRoot());
    }

    private class AssetSpinnerRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ImageAsset asset = (ImageAsset) value;
            JLabel label = (JLabel) super.getListCellRendererComponent(list, asset.getName(), index, isSelected, cellHasFocus);
            if (label == null ||
                iconImporterController == null) {
                return label;
            }
            File imageFile = iconImporterController.getThumbnailFile(asset);
            if (imageFile != null && imageFile.exists()) {
                label.setIcon(new ImageIcon(imageFile.getAbsolutePath()));
            }
            return label;
        }
    }

    private class AssetFormat extends Format {
        @Override
        public StringBuffer format(Object obj, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition pos) {
            if (obj != null) {
                toAppendTo.append(((ImageAsset) obj).getName());
            }
            return toAppendTo;
        }

        @Override
        public Object parseObject(String s, @NotNull ParsePosition parsePosition) {
            return null;
        }
    }
}