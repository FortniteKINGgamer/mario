package ai;

import mario.game.drawings.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList  = new ArrayList<>();
    public ArrayList<Node> pathList =  new ArrayList<>();
     Node startNode, goalNode,  currentNode;
     boolean goalReached  = false;
     int step = 0;

     public PathFinder(GamePanel gp){
         this.gp = gp;
         instatiateNodes();

     }
     public void instatiateNodes(){

         node = new Node[gp.maxWorldCol][gp.maxWorldRow];

         int col = 0;
         int row = 0;

         while (col < gp.maxWorldCol && row < gp.maxWorldRow){}{

             node[col][row] =  new Node(col,row);

             col++;
             if (col == gp.maxWorldCol){
                 col = 0;
                 row++;
             }
         }

     }
     public void resetNode(){

         int col = 0;
         int row = 0;

         while (col < gp.maxWorldCol && row < gp.maxWorldRow){

             // RESET OPEN, CHECKED AND SOLID STATE
             node[col][row].open = false;
             node[col][row].checked = false;
             node[col][row].solid = false;

             col++;
             if (col == gp.maxWorldCol){
                 col = 0;
                 row++;

             }
         }

         // Reset other settings;
         openList.clear();
         pathList.clear();

         goalReached = false;
         step = 0;
     }
     public void setNodes(int startCol, int startRow, int goalCol , int goalRow){

         resetNode();

         // Set start and Goal node;
         startNode = node[startCol][startRow];
         currentNode = startNode;
         goalNode = node[goalCol][goalRow];
         openList.add(currentNode);

         int col = 0;
         int row = 0;

         while (col < gp.maxWorldCol && row < gp.maxWorldRow){

             // SET SOLID NODE
             int tileNum = gp.getTileM().mapTileNum[gp.currentMap][col][row];
             if (gp.getTileM().tile[tileNum].collision){
                 node[col][row].solid = true;

             }
             // CHECK INTERACTIVE TILE
             for (int i = 0; i < gp.iTile[1].length; i++){
                 if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible){
                     int itCol = gp.iTile[gp.currentMap][i].worldX /gp.tileSize;
                     int itRow = gp.iTile[gp.currentMap][i].worldY /gp.tileSize;
                     node[itCol][itRow].solid = true;
                 }
             }
             //SET COST
             getCost(node[col][row]);

             col++;
             if (col == gp.maxWorldCol){
                 col = 0;
                 row++;
             }
         }
     }
     public void getCost(Node node){

         // G cost
         int xDistance = Math.abs(node.col - startNode.col);
         int yDistance = Math.abs(node.row - startNode.row);
         node.gCost = xDistance - yDistance;
         // H cost
         xDistance = Math.abs(node.col - goalNode.col);
         yDistance = Math.abs(node.row - goalNode.row);
         node.hCost = xDistance + yDistance;
         // F cost
         node.fCost = node.gCost + node.hCost;

     }
     public boolean search(){

         while (goalReached == false && step < 500){
             int col = currentNode.col;
             int row = currentNode.row;

             // CHECK THE CURRENT NODE
             currentNode.checked = true;
             openList.remove(currentNode);

             // OPEN THE UP NODE
             if (row - 1 >= 0 ) {
                 openNode(node[col][row -1]);
             }
             // OPEN LEFT NODE
             if (col -1 >= 0){
                 openNode(node[col-1][row]);
             }
             // Open the down Node
             if (row + 1< gp.maxWorldRow){
                 openNode(node[col][row+1]);
             }
             // Open Right Node
             if (col+1 < gp.maxWorldCol){
                 openNode(node[col+1][row]);
             }
             // FIND THE BEST NODE
             int bestNodeIndex= 0;
             int bestNodefCost = 999;

             for (int i = 0; i < openList.size(); i++){
                 // Check if node's f cost is better
                 if (openList.get(i).fCost < bestNodefCost){
                     bestNodeIndex = i;
                     bestNodefCost = openList.get(i).fCost;
                 }
                 // IF F COST is equal, check the g cost
                 else if(openList.get(i).fCost == bestNodefCost){
                     if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                         bestNodeIndex = i;
                     }
                 }
             }
             // IF THERE IS NO NODE IN THE OPEN LIST, END THE LOOP
             if (openList.size()== 0){
                 break;
             }
             // AFTER THE LOOP, OPENLIST[BESTNODEINDEX] IS THE NEXT STEP (= CURRENTNODE)
             currentNode = openList.get(bestNodeIndex);

             if (currentNode == goalNode){
                 goalReached = true;
                 trackThePath();
             }
             step++;
         }
         return goalReached;
     }
     public void openNode(Node node){

         if (!node.open && !node.checked && !node.solid){
             node.open = true;
             node.parent = currentNode;
             openList.add(node);

         }
     }
     public void trackThePath(){

         Node current = goalNode;

         while (current != startNode){

             pathList.add(0,current);
             current = current.parent;

         }
     }
}