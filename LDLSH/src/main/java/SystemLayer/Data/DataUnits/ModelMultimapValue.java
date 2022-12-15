package SystemLayer.Data.DataUnits;

import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.jetbrains.annotations.NotNull;

public record ModelMultimapValue(LSHHash lshHash,
                                 UniqueIdentifier uniqueIdentifier,
                                 ErasureBlock erasureCode
) implements MultiMapValue {

    public ModelMultimapValue(LSHHash lshHash, UniqueIdentifier uniqueIdentifier, ErasureBlock erasureCode) {
        this.lshHash = lshHash;
        this.uniqueIdentifier = uniqueIdentifier;
        this.erasureCode = erasureCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != ModelMultimapValue.class)
            return false;

        ModelMultimapValue modelMultimapValue = (ModelMultimapValue) obj;
        return lshHash.equals(modelMultimapValue.lshHash) &&
                uniqueIdentifier.equals(modelMultimapValue.uniqueIdentifier) &&
                erasureCode.equals(modelMultimapValue.erasureCode);
    }

    @Override
    public int hashCode() {
        return uniqueIdentifier.hashCode();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if( ! (o instanceof ModelMultimapValue) )
            return -1;
        return this.uniqueIdentifier.compareTo(((ModelMultimapValue) o).uniqueIdentifier);
    }
}
