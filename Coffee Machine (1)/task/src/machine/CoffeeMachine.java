package machine;

import java.util.Scanner;

public class CoffeeMachine {

    private enum CoffeeType {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6);

        public int water;
        public int milk;
        public int beans;
        public int cost;
        public final int cups = 1;

        CoffeeType(int w, int m, int b, int c) {
            water = w;
            milk = m;
            beans = b;
            cost = c;
        }
    }

    enum State {
        MENU, BUY, WATER, MILK, BEANS, CUPS
    }

    int water;
    int milk;
    int beans;
    int cups;
    int money;
    State state;

    private final String STR_PROMPT_ACTION = "Write action (buy, fill, take, remaining, exit):\n> ";
    final String STR_PROMPT_CHOICE = "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:\n> ";

    final String STR_PROMPT_WATER = "Write how many ml of water do you want to add:\n> ";
    final String STR_PROMPT_MILK = "Write how many ml of milk do you want to add:\n> ";
    final String STR_PROMPT_BEANS = "Write how many grams of coffee beans do you want to add:\n> ";
    final String STR_PROMPT_CUPS = "Write how many disposable cups of coffee do you want to add:\n> ";

    final String STR_TAKE = "I gave you $";

    public CoffeeMachine(int water, int milk, int beans, int cups, int money) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.money = money;
        state = State.MENU;
        System.out.print(STR_PROMPT_ACTION);
    }

    public boolean process(String input) {
        switch (state) {
            case MENU:
                boolean keepRunning = processMenu(input);
                if (!keepRunning) {
                    return false;
                }
                break;
            case BUY:
                processBuy(input);
                break;
            case WATER:
                this.water += Integer.parseInt(input);
                changeState(State.MILK);
                break;
            case MILK:
                this.milk += Integer.parseInt(input);
                changeState(State.BEANS);
                break;
            case BEANS:
                this.beans += Integer.parseInt(input);
                changeState(State.CUPS);
                break;
            case CUPS:
                this.cups += Integer.parseInt(input);
                changeState(State.MENU);
                break;
            default:
                break;
        }
        return true;
    }

    private void printState() {
        System.out.println();
        System.out.println("The coffee machine has:");
        System.out.println(this.water + " of water");
        System.out.println(this.milk + " of milk");
        System.out.println(this.beans + " of coffee beans");
        System.out.println(this.cups + " of disposable cups");
        System.out.println("$" + this.money + " of money");
    }

    private void tryToBuy(CoffeeType type) {
        if (this.water < type.water) {
            System.out.println("Sorry, not enough water!");
            changeState(State.MENU);
        } else if (this.milk < type.milk) {
            System.out.println("Sorry, not enough milk!");
            changeState(State.MENU);
        } else if (this.beans < type.beans) {
            System.out.println("Sorry, not enough beans!");
            changeState(State.MENU);
        } else if (this.cups < 1) {
            System.out.println("Sorry, not enough cups!");
            changeState(State.MENU);
        } else {
            System.out.println("I have enough resources, making you a coffee!");
            this.water -= type.water;
            this.milk -= type.milk;
            this.beans -= type.beans;
            this.cups -= type.cups;
            this.money += type.cost;
            changeState(State.MENU);
        }
    }

    private void processBuy(String input) {
        if (input.equals("back")) {
            changeState(State.MENU);
            return;
        }
        int choice = Integer.parseInt(input);
        CoffeeType type = CoffeeType.ESPRESSO;
        switch (choice) {
            case 1:
                type = CoffeeType.ESPRESSO;
                break;
            case 2:
                type = CoffeeType.LATTE;
                break;
            case 3:
                type = CoffeeType.CAPPUCCINO;
                break;
            default:
                break;
        }
        tryToBuy(type);
    }

    private boolean processMenu(String input) {
        switch (input) {
            case "buy":
                changeState(State.BUY);
                break;
            case "fill":
                changeState(State.WATER);
                break;
            case "take":
                System.out.println();
                System.out.println(STR_TAKE + this.money);
                this.money = 0;
                changeState(State.MENU);
                break;
            case "remaining":
                printState();
                changeState(State.MENU);
                break;
            case "exit":
                return false;
            default:
                changeState(State.MENU);
                break;
        }
        return true;
    }

    private void changeState(State next) {
        switch (next) {
            case MENU:
                state = State.MENU;
                System.out.println();
                System.out.print(STR_PROMPT_ACTION);
                break;
            case BUY:
                state = State.BUY;
                System.out.println();
                System.out.print(STR_PROMPT_CHOICE);
                break;
            case WATER:
                state = State.WATER;
                System.out.println();
                System.out.print(STR_PROMPT_WATER);
                break;
            case MILK:
                state = State.MILK;
                System.out.print(STR_PROMPT_MILK);
                break;
            case BEANS:
                state = State.BEANS;
                System.out.print(STR_PROMPT_BEANS);
                break;
            case CUPS:
                state = State.CUPS;
                System.out.print(STR_PROMPT_CUPS);
                break;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine machine = new CoffeeMachine(400, 540, 120, 9, 550);
        while (scanner.hasNext()) {
            if (!machine.process(scanner.next())) {
                return;
            }
        }
    }
}
