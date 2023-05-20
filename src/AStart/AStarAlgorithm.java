package AStart;

import Utilities.Board;

import java.util.*;

public class AStarAlgorithm {

    public int GetPathLength(int startIndex, int endIndex){
        var tilesNodes = new TileNode[36];
        for(var i = 0; i < tilesNodes.length; i++){
            tilesNodes[i] = new TileNode(i);
        }

        List<Integer> path = new ArrayList<>();

        TileNode startNode = tilesNodes[startIndex];

        PriorityQueue<TileNode> openSet = new PriorityQueue();
        Set<TileNode> closedSet = new HashSet();

        openSet.add(startNode);

        while (!openSet.isEmpty()){
            TileNode crrNode = openSet.poll();

            if(crrNode.GetIndex() == endIndex){
                var node = crrNode;
                while(node != null){
                    path.add(node.GetIndex());
                    node = node.GetParent();
                }
                return path.size();
            }

            closedSet.add(crrNode);
            for(var neighbourIndex : Board.NEIGHBOUR_MAP[crrNode.GetIndex()]){
                if(closedSet.contains(tilesNodes[neighbourIndex]))
                    continue;

                var gCost = crrNode.GetGCost() + 1;

                if(openSet.contains(tilesNodes[neighbourIndex])){
                    if(gCost < tilesNodes[neighbourIndex].GetGCost()){
                        tilesNodes[neighbourIndex].SetParent(crrNode);
                        tilesNodes[neighbourIndex].SetGCost(gCost);
                    }
                }
                else{
                    tilesNodes[neighbourIndex].SetParent(crrNode);
                    tilesNodes[neighbourIndex].SetGCost(gCost);
                    openSet.add(tilesNodes[neighbourIndex]);
                }
            }
        }
        return -1;
    }
}
