package service;

import materials.ClassNodeMaterial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DependencyPersistenceAutomaton {

    private ChangePropagationProcessService _process;
    private File _directory;
    private String _filename;
    private final ObservableList<File> _xmlfiles;

    public DependencyPersistenceAutomaton(ChangePropagationProcessService propagationProcess, final String directoryPath)
    {
        _process = propagationProcess;
        _directory = new File(directoryPath);
        _xmlfiles = FXCollections.observableArrayList();
        if(!_directory.isDirectory())
        {
            throw new IllegalArgumentException("Directory: '" + directoryPath + "' ist not a _directory");
        }
        _filename = createFilename();
    }


    public ObservableList<File> getXmlFileList()
    {
        return _xmlfiles;
    }

    public String getFilepath()
    {
        return _directory.getAbsolutePath() + _filename;
    }

    public String createFilename()
    {
        return "/" + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.xml'").format(new Date());
    }

    public List<File> getAllXmlFils()
    {
        List<File> xmlFileList = new ArrayList<>();
        File[] files = _directory.listFiles();
        for(File file: files)
        {
            if(file.getName().toLowerCase().endsWith(".xml"))
            {
                xmlFileList.add(file);
            }
        }
        return xmlFileList;
    }

    public void writeChangePropagationGraph() throws Exception
    {
        //String filename = getFilepath();
        String filename = _directory.getCanonicalPath() + "/test.xml";
        final JAXBContext context = JAXBContext.newInstance(ChangePropagationProcessService.class);
        final Marshaller marshaller = context.createMarshaller();

        try(final OutputStream os = new BufferedOutputStream(new FileOutputStream(filename)))
        {
            marshaller.marshal(_process, os);
        }
    }

    public void writeClassNodeMaterial(ClassNodeMaterial classNodeMaterial) throws Exception
    {
        String filename = _directory.getCanonicalPath() +"/testxml";
        final JAXBContext context = JAXBContext.newInstance(ClassNodeMaterial.class);
        final Marshaller marshaller = context.createMarshaller();

        try(final OutputStream os = new BufferedOutputStream(new FileOutputStream(filename)))
        {
            marshaller.marshal(classNodeMaterial,os);
        }
    }

    public static ClassNodeMaterial readClassNodeMaterial(final String filename) throws Exception {
        final JAXBContext context = JAXBContext.newInstance(ClassNodeMaterial.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();

        try (final InputStream in = new BufferedInputStream(new FileInputStream(filename)))
        {
            return (ClassNodeMaterial) unmarshaller.unmarshal(in);
        }
    }
}
