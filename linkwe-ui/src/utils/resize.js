
<script src="node_modules/element-resize-detector/dist/element-resize-detector.min.js"></script>
 var elemt = elementResizeDetectorMaker();
 var erdUltraFast = elementResizeDetectorMaker({
   strategy: "scroll" 
 });
 elemt.listenTo(document.getElementById("fatherbox"), function(element) {
   var width = element.offsetWidth;
   var height = element.offsetHeight;
   console.log("Size: " + width + "x" + height);
 });
 