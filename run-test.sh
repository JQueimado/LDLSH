#! /bin/bash

./run-quinta-servers.sh all -k
rm -rf TESTS* log_*

./run-test-quinta.sh . 10 SRR2400000aa SRR2400000aa > log_SRRaa_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR2400000aa SRR2400000ab > log_SRRaa_SRRab.txt 2>&1;
./run-test-quinta.sh . 10 SRR2400000aa SRR2400000aw > log_SRRaa_SRRaw.txt 2>&1;
