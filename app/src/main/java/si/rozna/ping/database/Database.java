package si.rozna.ping.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import si.rozna.ping.Constants;
import si.rozna.ping.database.dao.GroupDao;
import si.rozna.ping.models.db.GroupDbModel;

@androidx.room.Database(entities = {GroupDbModel.class}, version = 2, exportSchema = false)
public abstract class Database extends RoomDatabase {

    /* DAO references */
    public abstract GroupDao groupDao();

    /* Database static instance */
    private static volatile Database INSTANCE;

    /* Executor for accessing DB in background */
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static Database getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (Database.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    Database.class,
                                    Constants.DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
