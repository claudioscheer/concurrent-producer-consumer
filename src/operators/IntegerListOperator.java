package operators;

import interfaces.GenericListInterface;
import utils.RandomNumbers;

public class IntegerListOperator extends GenericOperator {

    private final GenericListInterface<Integer> list;

    public IntegerListOperator(GenericListInterface<Integer> list) {
        this.list = list;
    }

    @Override
    public boolean operateAdd() throws InterruptedException {
        return this.list.add(RandomNumbers.getRandomInt());
    }

    @Override
    public boolean operateRemove() throws InterruptedException {
        return this.list.remove(RandomNumbers.getRandomInt());
    }

    @Override
    public boolean operateContains() {
        return this.list.contains(RandomNumbers.getRandomInt());
    }

    @Override
    protected int operateListSize() {
        return this.list.size();
    }
}