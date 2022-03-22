package SystemLayer.Data.PackagesImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class StandardPackages implements Packages{
    @Override
    public void setData(UniqueIdentifier uniqueIdentifier, LSHHash lshHash, ErasureCodes erasureCodes, int n_packages) {

    }

    @Override
    public Package[] getPackages() {
        return new Package[0];
    }

    public class StandardPackage implements Package{

        @Override
        public void setData(UniqueIdentifier uniqueIdentifier, byte[] lshHashBlock, byte[] erasureBlock) {

        }

        @Override
        public byte[] getSerialized() {
            return new byte[0];
        }
    }
}
