package co_mute.worker;

import co_mute.model.CarPool;
import co_mute.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class WorkerController implements WorkerService {

    @Override
    public void AddCarpool(CarPool carPool) throws SQLException {
        Worker worker = new Worker();
        worker.CheckOverlappingTimes(carPool);
    }

    @Override
    public Map<String, Object> GetListCarPool(String email) throws Exception {
        Worker worker = new Worker();
        Map<String, Object> map = new HashMap<>();
        worker.GetJoinedPool(email);

        map.put("leader", worker.getLeader());
        map.put("departure", worker.getDeparture());
        map.put("origin", worker.getOrigin());
        map.put("created_at", worker.getCreated_at());
        map.put("destination", worker.getDestination());
        return map;
    }

    @Override
    public void AddUser(User user) throws SQLException {
        Worker worker = new Worker();
        worker.InsertUser(user);
    }

    @Override
    public boolean connect(String userEmail, String passwordFromForm) throws Exception {
        Worker worker = new Worker();
        if (worker.isUserExist(userEmail)) {
            return Worker.VerifyPassword(passwordFromForm, Worker.DB_PASSWORD);
        }
        return false;
    }

}
