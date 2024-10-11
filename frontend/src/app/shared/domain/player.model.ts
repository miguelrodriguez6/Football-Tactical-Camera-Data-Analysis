export interface PlayerModel{
  name: string;
  number: number;
  optaId: number;
  position: string;
}

export interface EventModel{
  alignedFrameid: number;
  game: any;
  id: number;
  minute: number;
  opPlayerId: number;
  outcome: number;
  player: any;
  second: number;
  side: string;
  typeId: number;
  x: number;
  y: number;
}
