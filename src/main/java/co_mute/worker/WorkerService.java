package co_mute.worker;

import co_mute.model.CarPool;
import co_mute.model.User;

import java.sql.SQLException;
import java.util.Map;

public interface WorkerService {
    void AddCarpool(CarPool carPool) throws SQLException;

    Map<String, Object> GetListCarPool(String email) throws Exception;

    //    Map<String,Object> GetUser(String email) throws Exception;
    void AddUser(User user) throws SQLException;

    boolean connect(String userEmail, String passwordFromForm) throws Exception;

}

