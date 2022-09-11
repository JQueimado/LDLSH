#! /bin/bash
./run-test-quinta.sh 100 Split1.txt Split2.txt > log_S1_S2.txt 2>&1;
./run-test-quinta.sh 100 Split2.txt Split1.txt > log_S2_S1.txt 2>&1;
./run-test-quinta.sh 100 jqueimado_QS_1000.txt jqueimado_QS_1000.txt > log_queimado_QS_1000.txt 2>&1;