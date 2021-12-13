package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

    static class Self {
        public String href;
    }

    static class Links {
        public Self self;
    }

    static class PlayerState {
        public Integer x;
        public Integer y;
        public String direction;
        public Boolean wasHit;
        public Integer score;
    }

    static class Arena {
        public List<Integer> dims;
        public Map<String, PlayerState> state;
    }

    static class ArenaUpdate {
        public Links _links;
        public Arena arena;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @GetMapping("/")
    public String index() {
        return "Let the battle begin!";
    }

    @PostMapping("/**")
    public String index(@RequestBody ArenaUpdate arenaUpdate) {
        System.out.println(arenaUpdate);
        String selfUrl = arenaUpdate._links.self.href;
        Integer width = arenaUpdate.arena.dims.get(0);
        Integer height = arenaUpdate.arena.dims.get(1);
        Integer SelfX = arenaUpdate.arena.state.get(selfUrl).x;
        Integer SelfY = arenaUpdate.arena.state.get(selfUrl).y;
        String direction = arenaUpdate.arena.state.get(selfUrl).direction;
        String currentLeftRight = checkIfNewCurrentLeftRight(SelfX, width);


        String middleCommand = goToMiddle(SelfY, height);
        if (middleCommand == "DOWN") {
            return goDown(direction);
        } else if (middleCommand == "UP") {
            return goUp(direction);
        }

        String leftRightCommand = leftOrRight(direction, currentLeftRight);
        String[] command = {leftRightCommand, "T"};
        return command[new Random().nextInt(command.length)];

    }

    public String goToMiddle(Integer selfY, Integer height) {
        Integer desiredX = height / 2;
        if (selfY > desiredX) {
            return "DOWN";
        } else if (selfY < desiredX) {
            return "UP";
        }
        return "GOOD";
    }

    public String goDown(String direction) {
        if (direction == "S") {
            return "F";
        } else if (direction == "W") {
            return "L";
        } else if (direction == "E") {
            return "R";
        }
        return "R";
    }
    public String goUp(String direction) {
        if (direction == "N") {
            return "F";
        } else if (direction == "W") {
            return "R";
        } else if (direction == "E") {
            return "L";
        }
        return "R";
    }
    public String leftOrRight(String direction, String currentLeftRight){

        if (currentLeftRight == "R"){
            if (direction == "E"){
                return "F";
            } else if (direction == "N"){
                return "R";
            } else if (direction == "S"){
                return "L";
            } else if(direction == "W"){
                return "R";
            }
        }
        if (currentLeftRight == "L"){
            if (direction == "W"){
                return "F";
            } else if (direction == "N"){
                return "L";
            } else if (direction == "S"){
                return "R";
            } else if(direction == "E"){
                return "R";
            }
        }
        return "T";
    }
    public String checkIfNewCurrentLeftRight(Integer selfX, Integer width){
        if (selfX > width /2){
            String[] poss = {"R", "L", "L"};
            return poss[new Random().nextInt(poss.length)];
        }
        String[] poss = {"R", "R", "L"};
        return poss[new Random().nextInt(poss.length)];
    }
}

