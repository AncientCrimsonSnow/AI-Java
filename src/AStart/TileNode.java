package AStart;

public class TileNode {
    private int _index;
    private int _gCost;
    private TileNode _parent;

    public TileNode(int index) {
        _index = index;
    }

    public int GetIndex(){
        return _index;
    }

    public int GetGCost() {
        return _gCost;
    }

    public void SetGCost(int gCost) {
        _gCost = gCost;
    }

    public TileNode GetParent(){
        return _parent;
    }

    public void SetParent(TileNode parent){
        _parent = parent;
    }
}
