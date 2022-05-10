function chooseUserType(tab) {
    const nav_tabs = document.getElementsByClassName('nav-link');

    Array.prototype.forEach.call(nav_tabs, function(nav_tab) {
        nav_tab.className = 'nav-link'

        if (nav_tab === tab) {
            nav_tab.className = 'nav-link active'
        }
    });

    const nav_tabContents = document.getElementsByClassName('tab-pane');

    Array.prototype.forEach.call(nav_tabContents, function (contents) {
        contents.className = 'tab-pane fade'

        if (tab.id === 'nav-athlete-tab') {
            document.getElementById('nav-athlete').className = 'tab-pane fade show active'
        }
        else if (tab.id === 'nav-coach-tab') {
            document.getElementById('nav-coach').className = 'tab-pane fade show active'
        }
        else if (tab.id === 'nav-organizer-tab') {
            document.getElementById('nav-organizer').className = 'tab-pane fade show active'
        }
        else if (tab.id === 'nav-nutritionist-tab') {
            document.getElementById('nav-nutritionist').className = 'tab-pane fade show active'
        }
    });

}