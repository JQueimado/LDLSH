set datafile separator ','
set boxwidth 0.5
set style fill solid
plot "/media/sf_Data/TESTS-100/TEST-LDLSH-Optimized-test-2_I-SRR100000shufaa_Q-SRR100000shufab_IT-10/latency.mean.csv" every 1 using (1.0):2 with boxes
pause -1 "Hit any key to continue"