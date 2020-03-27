t.forceManyBody=function(){
	function t(t){
		var n,a=i.length,c=sn(i,gn,_n).visitAfter(e);
		for(u=t,n=0;n<a;++n)
			o=i[n],c.visit(r)
	}
	function n(){
		if(i){
			var t,n,e=i.length;
			for(a=new Array(e),t=0;t<e;++t)
				n=i[t],a[n.index]=+c(n,t,i)
		}
	}
	function e(t){
		var n,e,r,i,o,u=0,c=0;
		if(t.length){
			for(r=i=o=0;o<4;++o)
				(n=t[o])&&(e=Math.abs(n.value))&&(u+=n.value,c+=e,r+=e*n.x,i+=e*n.y);
			t.x=r/c,t.y=i/c
		}else{
			(n=t).x=n.data.x,n.y=n.data.y;
			do{
				u+=a[n.data.index]
			}while(n=n.next)
		}
		t.value=u
	}
	function r(t,n,e,r){
		if(!t.value)return!0;
		var i=t.x-o.x,c=t.y-o.y,h=r-n,p=i*i+c*c;
		if(h*h/l<p)
			return p<f&&(0===i&&(i=gl(),p+=i*i),0===c&&(c=gl(),p+=c*c),p<s&&(p=Math.sqrt(s*p)),o.vx+=i*t.value*u/p,o.vy+=c*t.value*u/p),!0;
		if(!(t.length||p>=f)){
			(t.data!==o||t.next)&&(0===i&&(i=gl(),p+=i*i),0===c&&(c=gl(),p+=c*c),p<s&&(p=Math.sqrt(s*p)));
			do{
				t.data!==o&&(h=a[t.data.index]*u/p,o.vx+=i*h,o.vy+=c*h)
			}while(t=t.next)
		}
	}
	var i,o,u,a,c=vl(-30),s=1,f=1/0,l=.81;
	return 
	t.initialize=function(t){i=t,n()},
	t.strength=function(e){return arguments.length?(c="function"==typeof e?e:vl(+e),n(),t):c},
	t.distanceMin=function(n){return arguments.length?(s=n*n,t):Math.sqrt(s)},
	t.distanceMax=function(n){return arguments.length?(f=n*n,t):Math.sqrt(f)},
	t.theta=function(n){return arguments.length?(l=n*n,t):Math.sqrt(l)},
	t
}