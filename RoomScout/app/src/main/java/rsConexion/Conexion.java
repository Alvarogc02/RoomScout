package rsConexion;


import android.content.Context;
import android.os.AsyncTask;

import org.bson.Document;


import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class Conexion {

    static String appId = "roomscout-mqtwr";
    public static MongoDatabase mongoDatabase;
    static MongoClient mongoClient;
    public static MongoCollection<Document> mongoCollection;


    public static void conectarBD(Context context){

        Realm.init(context);
        App app = new App(new AppConfiguration.Builder(appId).build());
        Credentials credentials = Credentials.emailPassword("agarcab520@iesmartinezm.es","q1w2e3r4");

        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                app.login(credentials);
                mongoClient = app.currentUser().getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("RoomScout");
                return app.currentUser();
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
            }
        }.execute();
    }

    public static void conectarCollection(String collection) {

        mongoCollection = mongoDatabase.getCollection(collection);

    }

}

