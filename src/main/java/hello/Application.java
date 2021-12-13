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
        if (middleCommand.equals("DOWN")) {
            return goDown(direction);
        } else if (middleCommand.equals("UP")) {
            return goUp(direction);
        }

        String leftRightCommand = leftOrRight(direction, currentLeftRight);
        String[] command = {leftRightCommand, "T"};
        return command[new Random().nextInt(command.length)];

    }

    public String goToMiddle(Integer selfY, Integer height) {
        Integer desiredX = height / 2;
        System.out.println(desiredX);
        if (selfY > desiredX) {
            System.out.println("DOWN");
            return "DOWN";
        } else if (selfY < desiredX) {
            System.out.println("UP");

            return "UP";

        }
        System.out.println("GOOD");
        return "GOOD";

    }

    public String goDown(String direction) {
        switch (direction) {
            case "S":
                return "F";
            case "W":
                return "L";
            case "E":
                return "R";
        }
        return "R";
    }
    public String goUp(String direction) {
        switch (direction) {
            case "N":
                return "F";
            case "W":
                return "R";
            case "E":
                return "L";
        }
        return "R";
    }
    public String leftOrRight(String direction, String currentLeftRight){

        if (currentLeftRight.equals("R")){
            switch (direction) {
                case "E":
                    return "F";
                case "N":
                    return "R";
                case "S":
                    return "L";
                case "W":
                    return "R";
            }
        }
        if (currentLeftRight.equals("L")){
            switch (direction) {
                case "W":
                    return "F";
                case "N":
                    return "L";
                case "S":
                    return "R";
                case "E":
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

