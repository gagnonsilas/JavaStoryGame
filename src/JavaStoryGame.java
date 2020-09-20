import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class JavaStoryGame {

    Scanner scan = new Scanner(System.in);
    private int[] pos = {0, 0};

    List<String> inventory = new ArrayList<String>();


    Scanner roomScanner;

    {
        try {
            roomScanner = new Scanner(new BufferedReader(new FileReader("src/Maps/map1.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    List<List<List<String>>> maps = new ArrayList<>();

    String[][][] rooms = {};

    String[][][] roomBuildingBlocks =
            {
                    {
                            {
                                    "UP",
                                    "  ___| |___\n"
                            },
                            {
                                    "JP",
                                    "  ___|_|___\n"
                            },
                            {
                                    "",
                                    "  _________\n"
                            },
                    },
                    {
                            {
                                    "LEFT",
                                    "_|     ",
                                    "_     ",

                            },
                            {
                                    "JEFT",
                                    "_|     ",
                                    "_|    "

                            },
                            {
                                    "",
                                    " |     ",
                                    " |    ",

                            },
                    },
                    {
                            {
                                    "RIGHT",
                                    "    |_\n",
                                    "     _\n"
                            },
                            {
                                    "JIGHT",
                                    "    |_\n",
                                    "    |_\n",
                            },
                            {
                                    "",
                                    "    | \n",
                                    "    | \n",
                            }
                    },
                    {
                            {
                                    "DOWN",
                                    " |___   ___| \n",
                                    "     | |     \n",
                            },
                            {
                                    "jOWN",
                                    " |_________| \n",
                                    "     | |     \n",
                            },
                            {
                                    "",
                                    " |_________| \n",
                                    "             \n",
                            }
                    }
            };



    public static void main(String[] args)
    {
        JavaStoryGame jsg = new JavaStoryGame();
    }


    public JavaStoryGame()
    {
        runGame();
    }

    // Runs Game Loop
    public void runGame()
    {
        // starts the game in the first room
        printRoom();

        while(true)
        {
            //  Get user choice
            String input = scan.nextLine().toLowerCase();

            if(input.contains("quit"))
            {
                break;
            }

            if(doAction(input))
            {
                printRoom();
            }

        }
        System.out.println("Thanks for playing");
    }
    public void getRooms()
    {
        List<List<String>> roomRow = new ArrayList<>();
        String[] room = {};
        String line = "";
        while(roomScanner.hasNextLine())
        {
            while((line = roomScanner.nextLine()) != "~")
            {
                room = line.split(",");
                roomRow.add(Arrays.asList(room));
            }
        }
    }

    // Prints data from rooms array in a specific format when given room num input
    public void printRoom()
    {

        System.out.println(rooms[pos[0]][pos[1]][0] + "\n");

        System.out.println(buildRoom());

        for(int i = 1; i < rooms[pos[0]][pos[1]].length; i ++)
        {
            System.out.println(rooms[pos[0]][pos[1]][i] + "\n");
        }


    }

    public String buildRoom()
    {
        System.out.println("ROOM: ");

        String directions = rooms[pos[0]][pos[1]][2];
        String items = rooms[pos[0]][pos[1]][1];
        int[] roomObjs = new int[roomBuildingBlocks.length];
        String room = "";

        String item = " ";
        if(items.contains("KEY"))
        {
            item = "K";
        }

        if(directions.contains("DOOR") && !inventory.contains("KEY"))
        {
            directions = directions.substring(0, directions.indexOf("DOOR TO THE") + 12) + "J"+
                    directions.substring(directions.indexOf("DOOR TO THE") + 13, directions.length());
        }
        for(int i = 0;i < roomBuildingBlocks.length; i++)
        {
            for(int c = 0; c < roomBuildingBlocks[i].length; c++)
            {
                if(directions.contains(roomBuildingBlocks[i][c][0]))
                {
                    roomObjs[i] = c;
                    break;
                }
            }
        }

        room =  roomBuildingBlocks[0][roomObjs[0]][1] +
                roomBuildingBlocks[1][roomObjs[1]][1] + roomBuildingBlocks[2][roomObjs[2]][1] +
                roomBuildingBlocks[1][roomObjs[1]][2] + item + roomBuildingBlocks[2][roomObjs[2]][2] +
                roomBuildingBlocks[3][roomObjs[3]][1] +
                roomBuildingBlocks[3][roomObjs[3]][2];

        return(room);
    }

    public Boolean doAction(String input)
    {
        if(input.contains("go"))
        {
            return(go(input));
        }

        else if(input.contains("take"))
        {

            return(take(input));
        }
        else if(input.contains("restart"))
        {
            pos[0] = 0;
            pos[1] = 0;
            inventory = new ArrayList<String>();
            return(true);
        }
        else{
            System.out.println("Sorry I don't recognize that command\n\n");
            return(false);
        }
    }

    public Boolean take(String input)
    {
        if (input.contains(rooms[pos[0]][pos[1]][1].toLowerCase())){
            inventory.add(rooms[pos[0]][pos[1]][1]);
            System.out.println(rooms[pos[0]][pos[1]][1].toLowerCase() + " taken");
            rooms[pos[0]][pos[1]][1] = "";
            return(true);
        }
        else
        {
            System.out.println("That item is not in this room at the moment");
            return(false);
        }
    }

    public Boolean go(String input)
    {
        String directions = rooms[pos[0]][pos[1]][2];

        if(directions.contains("DOOR") && !inventory.contains("KEY"))
        {
            directions = directions.substring(0, directions.indexOf("DOOR TO THE") + 12) + "J"+
                    directions.substring(directions.indexOf("DOOR TO THE") + 13, directions.length());

        }


        if(input.contains("go"))
        {
            if(input.contains("up") && directions.contains("UP"))
            {
                pos[0] ++;
            }
            else if(input.contains("down") && directions.contains("DOWN"))
            {
                pos[0] --;
            }
            else if(input.contains("right") && directions.contains("RIGHT"))
            {
                pos[1] ++;
            }
            else if(input.contains("left") && directions.contains("LEFT"))
            {
                pos[1] --;
            }
            else if(directions.contains("DOOR"))
            {
                System.out.println("The door is locked. try to find a key for it \n");
                return(false);
            }
            else
            {
                System.out.println("You can't go that way right now \n");
                return(false);
            }
            return(true);
        }
        return(false);
    }
}