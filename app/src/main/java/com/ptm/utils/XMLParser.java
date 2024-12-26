package com.ptm.utils;

import com.ptm.model.Task;
import com.ptm.model.TaskManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class XMLParser {
    private final String filePath;

    public XMLParser(String filePath) {
        this.filePath = filePath;
    }

    public void saveTasks(TaskManager taskManager) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Tasks");
            doc.appendChild(root);

            for (Task task : taskManager.getTasks()) {
                Element taskElement = doc.createElement("Task");

                Element title = doc.createElement("Title");
                title.appendChild(doc.createTextNode(task.getTitle()));
                taskElement.appendChild(title);

                Element startDate = doc.createElement("StartDate");
                startDate.appendChild(doc.createTextNode(task.getStartDate().toString()));
                taskElement.appendChild(startDate);

                Element startTime = doc.createElement("StartTime");
                startTime.appendChild(doc.createTextNode(task.getStartTime().toString()));
                taskElement.appendChild(startTime);

                Element endDate = doc.createElement("EndDate");
                endDate.appendChild(doc.createTextNode(task.getEndDate().toString()));
                taskElement.appendChild(endDate);

                Element endTime = doc.createElement("EndTime");
                endTime.appendChild(doc.createTextNode(task.getEndTime().toString()));
                taskElement.appendChild(endTime);

                Element description = doc.createElement("Description");
                description.appendChild(doc.createTextNode(task.getDescription()));
                taskElement.appendChild(description);

                root.appendChild(taskElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TaskManager loadTasks() {
        TaskManager taskManager = new TaskManager();
        try {
            File file = new File(filePath);
            if (!file.exists()) return taskManager;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList taskNodes = doc.getElementsByTagName("Task");

            for (int i = 0; i < taskNodes.getLength(); i++) {
                Node taskNode = taskNodes.item(i);
                if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element taskElement = (Element) taskNode;

                    String title = taskElement.getElementsByTagName("Title").item(0).getTextContent();
                    LocalDate startDate = LocalDate.parse(taskElement.getElementsByTagName("StartDate").item(0).getTextContent());
                    LocalTime startTime = LocalTime.parse(taskElement.getElementsByTagName("StartTime").item(0).getTextContent());
                    LocalDate endDate = LocalDate.parse(taskElement.getElementsByTagName("EndDate").item(0).getTextContent());
                    LocalTime endTime = LocalTime.parse(taskElement.getElementsByTagName("EndTime").item(0).getTextContent());
                    String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();

                    Task task = new Task(title, startDate, startTime, endDate, endTime, description);
                    taskManager.addTask(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskManager;
    }
}
