import {Component, Input, OnInit} from "@angular/core"

@Component({
    moduleId: module.id,
    selector: "validator",
    templateUrl: "validator.component.html"

})

export class ValidatorComponent implements OnInit {

    private websocket;

    @Input() key : string;
    @Input() ekey: string;

    ledgerIndex: number;

    ledgerHash: string;

    constructor() {}

    ngOnInit() {
        this.init();
    }

    init() {
        this.websocket = new WebSocket("wss://livenet.xrpl.org/ws");

        console.log(this.key);
        console.log(this.ekey);

        this.websocket.onmessage = (msg) => {
            const data = JSON.parse(msg.data);
            if (data.type === 'validation' && (data.data.pubkey === this.ekey || data.data.pubkey === this.key)) {
                console.log(data.data);

                this.ledgerIndex = data.data.ledger_index;
                this.ledgerHash = data.data.ledger_hash;

                console.log(this.ledgerIndex);
            } else {
            }
        }

        this.websocket.onopen = (e) => {
            this.websocket.send('{"command": "subscribe", "streams": ["validations"], "pubkey": "n9LK6xCKVeHi5QiC7ZvUeqotf4m6JKH1oSk8tHBojiPxvXgA9haE"}');
        }
    }

}
