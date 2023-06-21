set terminal pdf size 18, 10 font ",30"

A = "#2F528F"
B = "#ED7D31"
C = "#000000"


set style line 1 lt rgb A lw 2 pt 9 ps 1 pi -2
set style line 2 lt rgb C lw 2 pt 1 ps 2 pi -2
set style line 3 lt rgb B lw 2 pt 7 ps 1 pi -2

set boxwidth 0.5
set style fill solid

#set logscale y 10
#set xrange [-0.5:5.5]

set ytics .1

set xtics ("class 10 with 3-gram" 0, \
    "class 10 with 4-gram" 1, \
    "class 10x10 with 3-gram" 2, \
    "class 10x10 with 4-gram" 3 )

############################################################################
set yrange [0:1]
set out "srrl3_new.pdf"
#set title "Mean of the best distances calcualted between data-set vectors \n concidering data-set class, configuration and ngram level"
set xlabel "Used data set configurations" 
set ylabel "Mean of the best distances calculated with error margins"


plot 'srrl3n.csv' using 1:3 title '' with boxes ls 1, \
    'srrl3n.csv' using 1:3:4:5 title '' with errorbars ls 2

############################################################################
#set yrange [.73:.97]
#set out "srrl4.pdf"

#plot 'srrl4.csv' every 2 using 1:3 title "a-b" with boxes ls 1, \
#    'srrl4.csv' every 2 using 1:3:4:5 title '' with errorbars ls 2, \
#    'srrl4.csv'   every 2::1 using 1:3 title 'a-c' with boxes ls 3, \
#    'srrl4.csv' every 2::1 using 1:3:4:5 title '' with errorbars ls 2, \