package omarihamza.utils;

import com.sun.istack.internal.Nullable;
import omarihamza.models.AppSettings;
import omarihamza.models.Contact;
import omarihamza.models.Group;

import java.io.*;
import java.util.ArrayList;

public class FileUtils {

    public static final String FILE_NAME = "data.bin";

    @Nullable
    public static ArrayList<Group> loadGroups() {

        ArrayList<Group> groups = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(FILE_NAME));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            while (true) {
                groups.add((Group) objectInputStream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {

        }

        return groups;

    }

    public static ArrayList<Group> storeGroup(String groupTitle, String groupDesc, ArrayList<Contact> contacts) {
        ArrayList<Group> groups = loadGroups();
        groups.add(new Group(groups.size() + 1, groupTitle, groupDesc, contacts));
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        storeGroups(groups, file);
        return groups;
    }

    public static void updateGroup(Group group) {
        ArrayList<Group> groups = loadGroups();
        for (Group group1 : groups) {
            if (group.getId() == group1.getId()) {
                group1.setContacts(group.getContacts());
                group1.setMessages(group.getMessages());
                break;
            }
        }
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        storeGroups(groups, file);
    }


    public static void storeGroups(ArrayList<Group> groups, File file) {
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            for (Group mGroup : groups) {
                objectOutputStream.writeObject(mGroup);
            }
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static AppSettings loadSettings() {
        File file = new File("data2.bin");
        AppSettings appSettings;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            appSettings = (AppSettings) objectInputStream.readObject();
            objectInputStream.close();
            return appSettings;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }


    public static void storeSettings(AppSettings settings){
        File file = new File("data2.bin");
        try {
            file.createNewFile();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(settings);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
