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

package materials;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;



public class ClassNodeFormatterTest {

    @Test
    public void testReverse()
    {
        Map<String, String> entries = new HashMap<>();

        entries.put("R", getFormattetString("R."));
        entries.put("R", getFormattetString("R$attr"));
        entries.put("R", getFormattetString("R$style"));
        entries.put("R", getFormattetString("R$drawable"));

        entries.put("NewsFeed", getFormattetString("de.unihamburg.hdw.model.news.NewsFeed (test.jar)"));
        entries.put("R", getFormattetString("de.unihamburg.hdw.R (test.jar)"));
        entries.put("Intent", getFormattetString("android.content.Intent (not found)"));
        entries.put("NewsDetailFragment", getFormattetString("de.unihamburg.hdw.viewController.news.NewsDetailFragment$1$1"));
        entries.put("String", getFormattetString("java.lang.String"));
        entries.put("R", getFormattetString("com.google.android.gms.R$drawable (test.jar)"));
        entries.put("C", getFormattetString("C.java"));

       //Start test
        Iterator it = entries.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            assertThat(pair.getValue(), is(pair.getKey()));
        }
    }

    public static String getFormattetString(final String classname)
    {
        return new ClassNodeFormatter(new ClassNode(classname)).toString();
    }
}
