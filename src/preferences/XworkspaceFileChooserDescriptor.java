/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package preferences;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

public class XworkspaceFileChooserDescriptor extends FileChooserDescriptor {
    public static final String FILENAME_EXTENSION = "xcworkspace";
    public XworkspaceFileChooserDescriptor(boolean chooseFiles, boolean chooseFolders, boolean chooseJars, boolean chooseJarsAsFiles, boolean chooseJarContents, boolean chooseMultiple) {
        super(false, true, false, false, false, false);
    }

    @Override
    public boolean isFileSelectable(VirtualFile file) {
        System.out.println(file.getName());
        return file.isDirectory() && file.getName().endsWith(FILENAME_EXTENSION);
    }
}
