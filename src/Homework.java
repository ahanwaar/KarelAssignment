import stanford.karel.*;
import java.util.ArrayList;

/**
 * Level 1: Put beepers only in the odd outside spots (e.g. 1x2, 2x1 are considered odd), then print the number how many you've put, and then
 * collect them all (do not duplicate beepers)
 * Level 2: Put beepers on all even spots, then print the number how many you've put, and finally collect them all
 * Level 3: Divide the map (using beepers) into 2 or 4 equal chambers (rectangles surrounded by walls or beepers),
 * depending on the map; see solution in 7x7; please note that you cannot put duplicate beepers.
 * Make sure to clean the map and print how many beepers you've put, then collect them all.
 */

public class Homework extends SuperKarel {
    private int xCoordinate, yCoordinate, xSize, ySize,beepersPlaced;
    ArrayList<Integer> xMed = new ArrayList<>();
    ArrayList<Integer> yMed = new ArrayList<>();

    public void run() {
        setBeepersInBag(10000);
        xCoordinate =1;
        yCoordinate =1;
        xSize =1;
        ySize =1;
        int lvl =1;
        int phase =1;
        boolean done = false;
        lvl=1;

        while (!done){
            while (lvl ==1){
                System.out.println("level: 1");
                while(phase ==1){
                    if(xCoordinate ==1 && yCoordinate ==1){
                        if (frontIsClear()) populateRow(); //first row
                        else {
                            turnLeft();
                            populateRow();
                        }
                        xSize = xCoordinate;
                    }
                    else { // the rest of rows
                        if (facingEast() && leftIsClear()){
                            turnLeft();
                            move();
                            if(frontIsClear()){
                                turnLeft();
                                populateMiddleRow();
                            }else {
                                turnLeft();
                                populateRow();
                                phase++;
                                goHome();
                            }

                        }else if (facingWest() && rightIsClear()){
                            turnRight();
                            move();
                            if(frontIsClear()){
                                turnRight();
                                populateMiddleRow();
                            }else {
                                turnRight();
                                populateRow();
                                phase++;
                                goHome();
                            }
                        }else {
                            phase++;
                            goHome();
                        }
                    }
                }
                while (phase ==2){
                    if(frontIsClear())cleanRow();
                    else{
                        if(beepersPresent()) pickBeeper();
                        if(xCoordinate ==1 && yCoordinate ==1 &&facingSouth()) {
                            lvl =2 ;
                            phase =1;
                            // for 8*1 map
                        }else if(facingEast() && leftIsBlocked()) {
                            turnAround();
                            move(xSize-1);
                            turnEast();//edit
                            ySize = yCoordinate;
                            lvl =2;
                            phase =1;

                        }else {
                            if(frontIsBlocked() && facingNorth()) ySize = yCoordinate;
                            // for 1*8 map
                            if(facingNorth() && frontIsBlocked() && rightIsBlocked() && leftIsBlocked()){
                                turnAround();
                                move(ySize-1);
                                turnEast();
                                lvl =2 ;
                                phase =1;
                            }
                        }
                        if(leftIsClear()) turnLeft();
                    }
                }
            }

            while (lvl==2){
                System.out.println("level: 2");
                while (phase ==1){
                    if(xSize >1)putBeeper();//edit
                    else turnEast();
                    while(leftIsClear()){
                        if (frontIsClear()){
                            move();
                            populateRow2();
                        } else {
                            if (facingEast() && xSize > 1){//edit
                                turnLeftAndMoveUp();
                            } else if (facingWest() && rightIsClear()){
                                turnRightAndMoveUp();
                            }else if(facingEast() && xSize ==1){//edit
                                turnLeft();
                                putBeeper_Move();
                                populateRow();
                                goHome();
                                lvl=2;
                                phase=2;
                                break;
                            } else {
                                //right top end corner
                                turnRight();
                                if(frontIsClear()){// 1*8 map
                                    cleanRow();
                                }else {
                                    phase =2;
                                    goHome();
                                    break;
                                }

                            }
                        }
                    }
                    //left top corner
                    while (facingEast() && leftIsBlocked() && frontIsClear()) {
                        if (beepersPresent()){
                            move();
                            populateRow();
                            phase =2;
                            goHome();
                            break;
                        } else {
                            populateRow2();
                            phase =2;
                            goHome();
                        }
                    }
                }

                while (phase == 2){
                    if(frontIsClear())cleanRow();
                    else if(xSize ==1 && frontIsBlocked()){
                        turnLeft();
                        cleanRow();
                        moveTo(1,1);
                        turnEast();
                        phase =1;
                        lvl =3;
                    }
                    else {
                        if(facingEast()){
                            turnLeft();
                            if(frontIsClear()){
                                move();
                                turnLeft();
                            }else {
                                lvl=3;
                                phase=1;
                                moveTo(1,1);
                                turnEast();
                            }
                        }else {
                            turnRight();
                            if (frontIsClear()){
                                move();
                                turnRight();
                            }else {
                                lvl=3;
                                phase=1;
                                moveTo(1,1);
                                turnEast();
                            }
                        }
                    }

                }
            }

            while (lvl ==3 && !done ){
                System.out.println("Level: "+ 3);
                this.xMed = getXMedian();
                this.yMed =getYMedian();
                if (phase==1){
                    if(xSize>1 && ySize >1){// 4 partitions
                        if(xSize % 2 == 0){//even
                            moveTo(xMed.get(1),1);
                            turnNorth();
                            populateLevel3();
                            turnLeft();
                            move();
                            turnSouth();
                            populateLevel3();
                            if(ySize % 2 ==0){
                                moveTo(1,yMed.get(1));
                                turnEast();
                                populateLevel3();
                                turnSouth();
                                move();
                                turnWest();
                                populateLevel3();
                                goHome();
                                phase =2;
                            }else if(ySize % 2 != 0){
                                moveTo(xSize,yMed.get(0));
                                turnWest();
                                populateLevel3();
                                goHome(); phase =2;
                            }
                        }else if(xSize % 2 != 0){
                            moveTo(xMed.get(0),1);
                            turnLeft();
                            populateLevel3();
                            if(ySize % 2 == 0){
                                moveTo(1,yMed.get(1));
                                turnEast();
                                populateLevel3();
                                turnSouth();
                                move();
                                turnWest();
                                populateLevel3();
                                goHome();
                                phase =2;
                            }else if(ySize % 2 != 0){
                                moveTo(xSize,yMed.get(0));
                                turnWest();
                                populateLevel3();
                                goHome(); phase =2;
                            }
                        }
                    }
                    else {
                        if(ySize == 1 ){
                            if (xMed.size() > 1){
                                moveTo(xMed.get(0),1);
                                putBeeper_Move();
                                putBeeper();
                                goHome();
                                phase=2;
                            }else {
                                moveTo(xMed.get(0),1);
                                putBeeper();
                                goHome();
                                phase=2;
                            }
                        }else if(xSize == 1){
                            if (yMed.size() > 1){
                                moveTo(1,yMed.get(0));
                                putBeeper_Move();
                                putBeeper();
                                goHome();
                                phase=2;
                            }else {
                                moveTo(1,yMed.get(0));
                                putBeeper();
                                goHome();
                                phase=2;
                            }
                        }
                    }
                }
                if (phase==2){
                    if(xSize>1 && ySize >1){// 4 partitions
                        if(xSize % 2 == 0){//even
                            moveTo(xMed.get(1),1);
                            turnNorth();
                            cleanRow();
                            turnLeft();
                            move();
                            turnSouth();
                            cleanRow();
                            if(ySize % 2 ==0){
                                moveTo(1,yMed.get(1));
                                turnEast();
                                cleanRow();
                                turnSouth();
                                move();
                                turnWest();
                                cleanRow();
                                moveTo(1,1);
                                done=true;
                            }else if(ySize % 2 != 0){
                                moveTo(xSize,yMed.get(0));
                                turnWest();
                                cleanRow();
                                moveTo(1,1);
                                done=true;
                            }
                        }else if(xSize % 2 != 0){
                            moveTo(xMed.get(0),1);
                            turnLeft();
                            cleanRow();
                            if(ySize % 2 == 0){
                                moveTo(1,yMed.get(1));
                                turnEast();
                                cleanRow();
                                turnSouth();
                                move();
                                turnWest();
                                cleanRow();
                                moveTo(1,1);
                                done=true;
                            }else if(ySize % 2 != 0){
                                moveTo(xSize,yMed.get(0));
                                turnWest();
                                cleanRow();
                                moveTo(1,1);
                                done=true;
                            }
                        }
                    } else {
                        if(ySize == 1 ){
                            if (xMed.size() > 1){
                                moveTo(xMed.get(0),1);
                                pickBeeper_Move();
                                pickBeeper();
                                moveTo(1,1);
                                done=true;
                            }else {
                                moveTo(xMed.get(0),1);
                                pickBeeper();
                                moveTo(1,1);
                                done=true;
                            }
                        }else if(xSize == 1){
                            if (yMed.size() > 1){
                                moveTo(1,yMed.get(0));
                                pickBeeper_Move();
                                pickBeeper();
                                moveTo(1,1);
                                done=true;
                            }else {
                                moveTo(1,yMed.get(0));
                                pickBeeper();
                                moveTo(1,1);
                                done=true;
                            }
                        }
                    }
                }
                turnEast();
            }
        }
    }

    //__________________BEEPERS METHODS_______________________
    @Override
    public void putBeeper(){
        while (beepersPresent())pickBeeper();
        super.putBeeper();
        beepersPlaced++;
    }

    public void putBeeper_Move(){
        while (beepersPresent())pickBeeper();
        putBeeper();
        if(frontIsClear()) move();
    }

    public void pickBeeper_Move(){
        while (beepersPresent())pickBeeper();
        if(frontIsClear()) move();
    }

    private void populateRow(){
        while (frontIsClear()){
            pickBeeper_Move();
            putBeeper_Move();
        }
    }

    private void populateRow2(){
        if (frontIsClear()){
            move();
            putBeeper();
        }
    }

    private void populateLevel3(){
        while (frontIsClear()){
            putBeeper_Move();
        }if(frontIsBlocked()) putBeeper();
    }

    private void populateMiddleRow(){
        if(xSize%2 ==0){
            while (frontIsClear()){
                while (beepersPresent()){
                    pickBeeper();
                }move();
            }putBeeper();
        }else {
            if(facingWest()){
                if(frontIsClear()){
                    putBeeper();
                    move();
                    while (frontIsClear()){
                        while (beepersPresent()){
                            pickBeeper();
                        }move();
                    }
                    putBeeper();
                }else {
                    putBeeper();
                }
            }else{
                while (frontIsClear()){
                    while (beepersPresent()){
                        pickBeeper();
                    }move();
                }if(beepersPresent()) pickBeeper();
            }
        }
    }

    public void cleanRow(){
        while (frontIsClear()){
            if(beepersPresent())pickBeeper_Move();
            else move();
        }if(frontIsBlocked()&& beepersPresent()) pickBeeper();
    }

    //__________________________MOVEMENTS METHODS____________________
    @Override
    public void move(){
        move(1);
    }

    public void move(int steps) {
        for(int i = 0; i < steps; i++) {
            super.move();
        }

        if(facingNorth()) {
            yCoordinate += steps;
        }
        else if(facingSouth()) {
            yCoordinate -= steps;
        }
        else if(facingEast()) {
            xCoordinate += steps;
        }
        else if(facingWest()) {
            xCoordinate -= steps;
        }
    }

    public void moveTo(int x, int y){
        int xDifference = x - xCoordinate;
        int yDifference = y - yCoordinate;

        if(xDifference > 0) {
            turnEast();
        }
        else if(xDifference < 0) {
            turnWest();
        }
        move(Math.abs(xDifference));

        if(yDifference > 0) {
            turnNorth();
        }
        else if(yDifference < 0) {
            turnSouth();
        }
        move(Math.abs(yDifference));
    }

    public void goHome(){
        System.out.println("Num. of Placed Beepers: " + beepersPlaced);
        moveTo(1,1);
        turnEast();
        beepersPlaced=0;
    }

    public ArrayList<Integer> getXMedian(){
        ArrayList<Integer> xMed= new ArrayList<>();
        if(xSize%2==0){
            xMed.add((xSize / 2 ));
            xMed.add((xSize / 2) + 1);
        }else {
            xMed.add((int)(((xSize/2)+1)));
        }
        return xMed;
    }

    public ArrayList<Integer> getYMedian(){
        ArrayList<Integer> yMed= new ArrayList<>();
        if(ySize%2==0){
            yMed.add((ySize / 2));
            yMed.add((ySize / 2) +1);
        }else {
            yMed.add((int)(((ySize/2)+1)));
        }
        return yMed;
    }

    //________________________ Directions Methods_________________
    private void turnNorth(){
        while (!facingNorth()){
            turnLeft();
        }
    }

    private void turnEast(){
        turnNorth();
        turnRight();
    }

    private void turnSouth(){
        turnEast();
        turnRight();
    }

    private void turnWest(){
        turnNorth();
        turnLeft();
    }

    private void turnLeftAndMoveUp(){
        turnLeft();
        if (frontIsClear() && beepersPresent()){
            move();
            turnLeft();
            populateRow2();
        }else if (frontIsClear() && noBeepersPresent()) {
            populateRow2();
            turnLeft();
        }
    }

    private void turnRightAndMoveUp(){
        turnRight();
        if (frontIsClear()){
            move();
            putBeeper();
        }
        turnRight();
    }
}


