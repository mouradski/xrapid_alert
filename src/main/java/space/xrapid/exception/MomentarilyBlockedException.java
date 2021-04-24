package space.xrapid.exception;

public class MomentarilyBlockedException extends RuntimeException {

    public MomentarilyBlockedException() {
        super("Momentarily blocked for exceeding the maximum number of calls per second (60).");
    }
}
