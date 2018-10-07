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

package changepropagation;

import dotparser.ClassNodeMaterial;
import dotparser.DependencyIF;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangePropagationService{
    private final ChangePropagationModelService _model;
    private final Set<ClassNodeMaterial> _affectedClassesByChange;
    public ChangePropagationService(final List<DependencyIF> classdependencies)

    {
        this._model = new ChangePropagationModelService(classdependencies);
        this._affectedClassesByChange = new HashSet<>();
    }

    public void setChangedClass(final ClassNodeMaterial changedClass)
    {
       // _model.
    }





}
