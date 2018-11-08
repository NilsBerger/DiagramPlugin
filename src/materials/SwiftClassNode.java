package materials;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SwiftClass")
public class SwiftClassNode extends ClassNode {
    public SwiftClassNode(String className) {
        super(className);
    }
}
