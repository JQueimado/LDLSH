#!/usr/bin/gnuplot
reset
fontsize = 10
#set term postscript enhanced eps fontsize solid

#set autoscale
#set title "Percentage of Sensitive Reads"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"
set size ratio 0.65
set xrange [0.74:0.96]
set yrange [5:130]

set logscale y 10

set lmargin 0.3
set rmargin 0.3

A = "#000000"
B = "#102631"
C = "#204d63"
D = "#317496"
E = "#499ac2"
F = "#7eb7d3"
G = "#afd2e4"

#A = "#222222"
#B = "#444444"
#C = "#666666"
#D = "#888888"
#E = "#AAAAAA"
#F = "#CCCCCC"

#set ytics 0.01,0.1,1
#set logscale x 2
#set logscale y
#set logscale y2
#set boxwidth 0.05
#set style fill solid
#set yrange [0:14982406523]
#f(x) = exp(-x**2 / 2)
#plot [t=-4:4] f(t), t**2 / 16
#set ytics 0,10,100 nomirror
#set ytics ("1%%" 1, "10%%" 10, "100%%" 100)
#set y2tics ("2.5%%" 2.5, "5%%" 5, "7.5%%" 7.5, "10%%" 10, "20%%" 20)
#set ytics (250, 500, 1000, 1500, 2000, 2500) nomirror
#set ytics ("0.4" 0.4,"0.5" 0.5, 1,2,4,8,16) nomirror
#set xtics out nomirror
#set ytics nomirror 
set grid ytics

set style line 1 lt rgb C lw 2	 pt 9 ps 1 pi -2
set style line 2 lt rgb C lw 2 pt 9 ps 1 pi -2
set style line 3 lt rgb D lw 2 pt 7 ps 1 pi -2
set style line 4 lt rgb D lw 2 pt 7 ps 1 pi -2
set style line 5 lt rgb F lw 2	 pt 5 ps 1 pi -2
set style line 6 lt rgb F lw 2 pt 5 ps 1 pi -2
set style line 7 lt rgb A lw 2	 pt 5 ps 1 pi -2
set style line 8 lt rgb A lw 2 pt 5 ps 1 pi -2


##set style line 6 lt rgb B lw 1 pt 6 pi -1 ps 1
#set style line 7 lt rgb C lw 4 pt 8 pi -1 ps 1
#set style line 8 lt rgb D lw 4 pt 6 pi -1 ps 1
#set style line 9 lt rgb E lw 4 pt 4 pi -1 ps 1
##set style line 10 lt rgb F lw 1 pt 6 pi -1 ps 1
#set style line 11 lt rgb G lw 4 pt 7 ps 1

#set style line 5 lt rgb A lw 4 pt 6 pi -1 ps 2
#set style line 11 lt rgb G lw 4 pt 7 ps 2

#set style line 6 lt rgb A lw 2 pt 6 pi -1 ps 2
#set style line 12 lt rgb G lw 2 pt 7 ps 2

set key right top 
##set key vertical maxrows 3 left
#set format y "%.0s"	
#set format x "%g%%"
#set linetype cycle 1
#set linetype cycle 2
#set linetype cycle 3
#set linetype cycle 4

set terminal pdf size 18, 10
set out "ThresholdxLatency.pdf"

#set autoscale y

plot 'ThresholdxLatency.csv' index 0 using 4:1:2:3:xtic(4):ytic(1) title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3:xtic(4):ytic(1) title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3:xtic(4):ytic(1) title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3:xtic(4):ytic(1) title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

#set output "ThresholdxLatency.ps"
#pause -1

#set term postscript
#set output "latencies_w.ps"
#replot    
#set term x11
