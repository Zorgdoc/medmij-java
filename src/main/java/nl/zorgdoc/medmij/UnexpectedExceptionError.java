package nl.zorgdoc.medmij;

class UnexpectedExceptionError extends RuntimeException {
    public UnexpectedExceptionError(java.lang.Throwable cause) {
        super("Unexpected " + cause.getClass().getName(), cause);
    }
}
