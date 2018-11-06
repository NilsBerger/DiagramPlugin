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

package service;

import materials.ClassNodeMaterial;

import java.util.Set;

public class StrictChangeAndFixStrategy implements ChangeAndFixStrategyIF{
    private int _allowedSize = 0;
    @Override
    public boolean accept(Set<ClassNodeMaterial> affectedClasses) {
        return affectedClasses.size() == _allowedSize;
    }
}
