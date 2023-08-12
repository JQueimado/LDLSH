#!/usr/bin/gnuplot
reset

A = "#2F528F"
B = "#ED7D31"
C = "#70AD47"
D = "#A5A5A5"
F = '#000'


set style line 1 lt rgb A lw 7 pt 9 ps 3 pi -2
set style line 2 lt rgb A lw 7 pt 9 ps 3 pi -2
set style line 3 lt rgb B lw 7 pt 7 ps 3 pi -2
set style line 4 lt rgb B lw 7 pt 7 ps 3 pi -2
set style line 5 lt rgb C lw 7 pt 5 ps 3 pi -2
set style line 6 lt rgb C lw 7 pt 5 ps 3 pi -2
set style line 7 lt rgb D lw 7 pt 43 ps 3 pi -2
set style line 8 lt rgb D lw 7 pt 43 ps 3 pi -2
set style line 9 lt rgb F lw 7 dt 2

set terminal pdf color font ",60" size 18, 10
set size ratio .5

set style data histogram
set style histogram cluster gap 1
set style fill solid
set boxwidth .9

set xtics ( \
    "server1" 0, \
    "server2" 1,  \
    "server3" 2  \
 )

set yrange [8800000:18000000]
set out "10-10-75-mem_graph.pdf"
plot "10-10-75-mem_graph.csv" using 1 title"LDLSH Optimized" ls 1,\
    '' using 2 title "LDLSH" ls 3,\
    '' using 3 title "Traditional External" ls 5,\
    '' using 4 title "Traditional Replicated" ls 7

set yrange [33000000:120000000]
set out "100-100-75-mem_graph.pdf"
plot "100-100-75-mem_graph.csv" using 1 title"LDLSH Optimized" ls 1,\
    '' using 2 title "LDLSH" ls 3,\
    '' using 3 title "Traditional External" ls 5,\
    '' using 4 title "Traditional Replicated" ls 7

set yrange [8000000:32000000]
set out "10x10-10x10-75-mem_graph.pdf"
plot "10x10-10x10-75-mem_graph.csv" using 1 title"LDLSH Optimized" ls 1,\
    '' using 2 title "LDLSH" ls 3,\
    '' using 3 title "Traditional External" ls 5,\
    '' using 4 title "Traditional Replicated" ls 7

# set key left top
# set xlabel "LSH index servers"
# set ylabel "Ressorce utilization in Mib"

# set out "10a-10a-mem_graph.pdf"
# plot '10a-10a-mem_graph.csv' using 1 title "LDLSH Optimized" ls 1,\
#     '' using 2 title "LDLSH" ls 3,\
#     '' using 3 title "Traditional External" ls 5,\
#     '' using 4 title "Traditional Replicated" ls 7

# set out "10x10a-10x10a-mem_graph.pdf"
# plot '10x10a-10x10a-mem_graph.csv' using 1 title "LDLSH Optimized" ls 1,\
#     '' using 2 title "LDLSH" ls 3,\
#     '' using 3 title "Traditional External" ls 5,\
#     '' using 4 title "Traditional Replicated" ls 7