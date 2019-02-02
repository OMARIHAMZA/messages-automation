package omarihamza.utils;

import com.sun.istack.internal.Nullable;
import omarihamza.models.Group;

import java.io.*;
import java.util.ArrayList;

public class FileUtils {

    private static final String FILE_NAME = "data.bin";

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

    public static ArrayList<Group> storeGroup(Group group) {
        ArrayList<Group> groups = loadGroups();
        groups.add(group);
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
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
        return groups;
    }

}
