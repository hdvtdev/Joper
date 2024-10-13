package hdvtdev.Discord.Notification;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hdvtdev.Tools.Annotations.Experimental;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Experimental
@Deprecated
public class UserDataManager {

    private static final List<UserData> userDataList = new ArrayList<>();
    private static final String FILE_PATH = "src/main/resources/userData.json";  // File to store the JSON data
    private static final Gson gson = new Gson();

    // Method to add new user data
    public static void addUserData(SlashCommandInteractionEvent event) {
        UserData userData = new UserData(event.getUser().getId(), event.getOption("group").getAsString(), event.getOption("dayofweek").getAsString());
        userDataList.add(userData);
        updateUserDataJSON();
    }

    public static void addUserData(UserData userData) {
        userDataList.add(userData);
        updateUserDataJSON();
    }

    public static ArrayList<UserData> getUserData() {
        return new ArrayList<>(userDataList);
    }

    public static UserData[] getUserDataFromJSON() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<UserData>>() {}.getType();
            List<UserData> loadedData = gson.fromJson(reader, listType);
            if (loadedData != null) {
                userDataList.addAll(loadedData);
            }
            return userDataList.toArray(new UserData[0]);
        } catch (FileNotFoundException e) {
            return new UserData[0];
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new UserData[0];
        }
    }

    public static void updateUserDataJSON() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(userDataList, writer);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void removeUserData(UserData userData) {
        userDataList.remove(userData);
        updateUserDataJSON();
    }
}
