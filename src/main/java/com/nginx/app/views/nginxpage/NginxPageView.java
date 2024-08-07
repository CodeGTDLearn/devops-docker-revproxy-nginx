package com.nginx.app.views.nginxpage;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("NginxPage")
@Route(value = "rota")
@RouteAlias(value = "")
public class NginxPageView extends VerticalLayout {

  String hostname = System.getenv("HOSTNAME");

  public NginxPageView() {

    setSpacing(false);

    Image img = new Image("images/empty-plant.png", "placeholder plant");
    img.setWidth("200px");
    add(img);

    H2 header = new H2("this is my contaneiner/host: " + hostname);
    header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
    add(header);
    add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

    setSizeFull();
    setJustifyContentMode(JustifyContentMode.CENTER);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    getStyle().set("text-align", "center");
  }

}