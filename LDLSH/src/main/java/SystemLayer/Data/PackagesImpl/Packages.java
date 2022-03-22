package SystemLayer.Data.PackagesImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.io.Serializable;

public interface Packages {
    void setData(
            UniqueIdentifier uniqueIdentifier,
            LSHHash lshHash,
            ErasureCodes erasureCodes,
            int n_packages
    );

    Package[] getPackages();

    interface Package extends Serializable {
        void setData(
                UniqueIdentifier uniqueIdentifier,
                byte[] lshHashBlock,
                byte[] erasureBlock
        );

        byte[] getSerialized();
    }
}
