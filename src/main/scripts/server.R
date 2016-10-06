# This is the server logic for a Shiny web application.
# You can find out more about building applications with Shiny here:
#
# http://shiny.rstudio.com
#

library(shiny)
car_colors <- read.csv("/resources/data/cars/colors.csv",head=TRUE,sep=",")
color_codes <- c("Punainen" = "red","Sininen" = "blue","Musta" = "black","Valkoinen" ="white","Harmaa" ="grey","Vihreä" ="green","Hopea"="azure2","Keltainen"="yellow")
car_colors <- car_colors[car_colors$vari %in% c("Punainen","Musta","Valkoinen","Harmaa","Sininen","Hopea","Vihreä","Keltainen"),]
shinyServer(function(input, output) {
  output$colorPlot <- renderPlot({
    ggplot(car_colors[car_colors$kaupunki == input$variable,],aes(x = vuosi,y = lkm,color = vari)) + stat_sum(aes(group = vari)) + scale_colour_manual(
       values = color_codes)
    
  })

})
