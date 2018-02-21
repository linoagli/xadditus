/**
 * Copyright (C) 2015 Olubusayo K. Faye-Lino Agli.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by linoagli on 2/5/15.
 * xadditus App Project.
 *
 */
package com.linoagli.java.xadditus.controllers;

import com.linoagli.java.xadditus.NirCmdHelper;
import com.linoagli.java.xadditus.Services;
import com.linoagli.library.javafx.base.BaseApplication;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends BaseApplication
{
    public static final String APP_NAME = "Xadditus";
    public static final String APP_VERSION_NAME = "Cassius";
    public static final int APP_VERSION_CODE = 1;

    public static final String RES_IMAGES = "/resources/images/";

    public static final String IMAGE_APP_ICON = RES_IMAGES + "ic_app.png";

    private TrayIcon trayIcon;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    protected void initApp() throws Exception {}

    @Override
    protected void initPrimaryStage() {}

    @Override
    protected void startApp() throws Exception
    {
        initTrayIcon();
        Services.start();
    }

    @Override
    protected void cleanUp()
    {
        Services.stop();
        dismissTrayIcon();
    }

    private void initTrayIcon() throws AWTException
    {
        if (!SystemTray.isSupported()) {
            return;
        }

        MenuItem mi_quit = new MenuItem("Quit");
        mi_quit.addActionListener(e -> closeApp());

        PopupMenu popup = new PopupMenu();
        popup.add(mi_quit);

        ImageIcon icon = new ImageIcon(getClass().getResource(IMAGE_APP_ICON));
        trayIcon = new TrayIcon(icon.getImage(), APP_NAME, popup);
        trayIcon.setImageAutoSize(true);

        SystemTray.getSystemTray().add(trayIcon);
    }

    private void dismissTrayIcon()
    {
        if (trayIcon == null) {
            return;
        }

        SystemTray.getSystemTray().remove(trayIcon);
    }
}
